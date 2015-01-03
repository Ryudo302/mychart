package br.com.colbert.mychart.aplicacao.artista;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.dominio.artista.service.ArtistaWs;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.ui.artista.ArtistaView;
import br.com.colbert.mychart.ui.comum.messages.*;

/**
 * Controlador de ações referentes a artistas.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@ApplicationScoped
public class ArtistaController implements Serializable {

	private static final long serialVersionUID = 2909647942422289099L;

	private static final class ArtistasPorNomeComparator implements Comparator<Artista> {

		@Override
		public int compare(Artista artista1, Artista artista2) {
			return new CompareToBuilder().append(artista1.getNome(), artista2.getNome()).toComparison();
		}
	}

	@Inject
	private Logger logger;

	@Inject
	private ArtistaView view;
	@Inject
	private MessagesView messagesView;

	@Inject
	private ArtistaRepository repositorio;
	@Inject
	private ArtistaWs artistaWs;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	public void consultarExistentes(@Observes @OperacaoCrud(TipoOperacaoCrud.CONSULTA) Artista exemplo) {
		logger.info("Consultando artistas com base em exemplo: {}", exemplo);
		List<Artista> artistas = new ArrayList<>();
		List<Artista> artistasUniqueList = SetUniqueList.setUniqueList(artistas);

		logger.debug("Consultando no repositório local");
		artistasUniqueList.addAll(consultarRepositorio(exemplo));

		logger.debug("Consultando na web");
		artistasUniqueList.addAll(consultarWeb(exemplo));

		logger.debug("Ordenando artistas");
		artistas.sort(new ArtistasPorNomeComparator());

		logger.debug("Definindo artistas na visão: {}", artistasUniqueList);
		view.setArtistas(artistas);
		messagesView.adicionarMensagemSucesso("Foi(ram) encontrado(s) " + artistas.size() + " artista(s)");
	}

	private Collection<Artista> consultarWeb(Artista exemplo) {
		Collection<Artista> artistas = CollectionUtils.emptyCollection();
		try {
			artistas = artistaWs.consultarPor(exemplo);
			logger.debug("Resultado web: {}", artistas);
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

	@Transactional
	public void adicionarNovo(@Observes @OperacaoCrud(TipoOperacaoCrud.INSERCAO) Artista artista) {
		logger.info("Adicionando: {}", artista);

		try {
			validar(artista);
			repositorio.adicionar(artista);
			messagesView.adicionarMensagemSucesso("Artista incluído com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (ElementoJaExistenteException exception) {
			logger.debug("Artista já existente", exception);
			messagesView.adicionarMensagemAlerta("Já existe um artista com o nome informado.");
		} catch (RepositoryException exception) {
			logger.error("Erro ao adicionar artista: " + artista, exception);
			messagesView.adicionarMensagemErro("Erro ao adicionar artista", exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void removerExistente(@Observes @OperacaoCrud(TipoOperacaoCrud.REMOCAO) Artista artista) {
		logger.info("Removendo: {}", artista);

		if (messagesView.exibirConfirmacao(MessageFormat.format("Deseja realmente excluir o artista {0}?", artista.getNome())) == RespostaConfirmacao.SIM) {
			try {
				validar(artista);
				repositorio.remover(artista);
				messagesView.adicionarMensagemSucesso("Artista removido com sucesso!");
			} catch (ValidacaoException exception) {
				logger.debug("Erros de validação", exception);
				messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
			} catch (RepositoryException exception) {
				logger.error("Erro ao remover artista: " + artista, exception);
				messagesView.adicionarMensagemErro("Erro ao remover artista", exception.getLocalizedMessage());
			}
		}
	}

	private void validar(Artista artista) throws ValidacaoException {
		logger.debug("Validando: {}", artista);
		Objects.requireNonNull(artista, "O artista não foi informado");

		for (Validador<Artista> validador : validadores) {
			validador.validar(artista);
		}
	}
}
