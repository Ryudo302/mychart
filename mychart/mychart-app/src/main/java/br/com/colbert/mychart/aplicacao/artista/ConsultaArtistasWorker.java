package br.com.colbert.mychart.aplicacao.artista;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.dominio.artista.service.ArtistaWs;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.infraestrutura.lastfm.ServicoInacessivelException;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;
import br.com.colbert.mychart.ui.artista.ArtistaPanel;

/**
 * Executor de consulta de artistas.
 * 
 * @author Thiago Colbert
 * @since 09/01/2015
 */
public class ConsultaArtistasWorker extends AbstractWorker<Collection<Artista>, Void> {

	private static final class ArtistasPorNomeComparator implements Comparator<Artista> {

		@Override
		public int compare(Artista artista1, Artista artista2) {
			return new CompareToBuilder().append(artista1.getNome(), artista2.getNome()).toComparison();
		}
	}

	@Inject
	private Logger logger;

	@Inject
	private ArtistaPanel view;

	@Inject
	private ArtistaRepository repositorio;
	@Inject
	private ArtistaWs artistaWs;

	private Artista exemplo;
	private boolean executou;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	@Override
	protected Collection<Artista> doInBackground() throws Exception {
		if (Objects.isNull(exemplo)) {
			throw new IllegalStateException("O artista de exemplo não foi informado");
		} else if (StringUtils.isBlank(exemplo.getNome())) {
			messagesView.adicionarMensagemAlerta("Informe um nome a ser consultado.");
			return CollectionUtils.emptyCollection();
		} else {
			logger.info("Consultando artistas com base em exemplo: {}", exemplo);
			List<Artista> artistas = new ArrayList<>();
			List<Artista> artistasUniqueList = SetUniqueList.setUniqueList(artistas);

			logger.debug("Consultando no repositório local");
			artistasUniqueList.addAll(consultarRepositorio(exemplo));

			logger.debug("Consultando na web");
			artistasUniqueList.addAll(consultarWeb(exemplo));

			logger.debug("Ordenando artistas");
			artistas.sort(new ArtistasPorNomeComparator());

			executou = true;
			return artistasUniqueList;
		}
	}

	private Collection<Artista> consultarWeb(Artista exemplo) {
		Collection<Artista> artistas = CollectionUtils.emptyCollection();
		try {
			artistas = artistaWs.consultarPor(exemplo);
			logger.debug("Resultado web: {}", artistas);
		} catch (ServicoInacessivelException exception) {
			logger.error("Erro ao consultar artistas na web", exception);
			messagesView.adicionarMensagemAlerta(MessageFormat.format("{0}\n\n{1}",
					"O serviço da LastFM está inacessível no momento.", "Consultando apenas os artistas já salvos localmente."));
		} catch (ServiceException exception) {
			logger.error("Erro ao consultar artistas na web a partir do exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar artistas", exception.getLocalizedMessage());
		}
		return artistas;
	}

	private Collection<Artista> consultarRepositorio(Artista exemplo) {
		Collection<Artista> artistas = CollectionUtils.emptyCollection();
		try {
			artistas = repositorio.consultarPor(exemplo);
			logger.debug("Resultado repositório: {}", artistas);
		} catch (RepositoryException exception) {
			logger.error("Erro ao consultar artistas no repositório a partir do exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar artistas", exception.getLocalizedMessage());
		}
		return artistas;
	}

	@Override
	protected void done() {
		if (executou) {
			try {
				Collection<Artista> artistas = get();
				view.setArtistas(artistas);
				messagesView.adicionarMensagemSucesso("Foi(ram) encontrado(s) " + artistas.size() + " artista(s)");
			} catch (InterruptedException | ExecutionException exception) {
				logger.error("Erro ao consultar artistas", exception);
				messagesView.adicionarMensagemErro("Erro ao consultar artistas", exception.getLocalizedMessage());
			}
		}
	}

	public Artista getExemplo() {
		return exemplo;
	}

	public void setExemplo(Artista exemplo) {
		this.exemplo = exemplo;
	}
}
