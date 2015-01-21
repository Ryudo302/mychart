package br.com.colbert.mychart.aplicacao.artista;

import java.util.*;
import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.dominio.artista.service.ArtistaWs;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;

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
	private ArtistaRepository repositorio;
	@Inject
	private ArtistaWs artistaWs;

	private Artista exemplo;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	@Override
	protected Collection<Artista> doInBackground() throws Exception {
		verificarSeExemploFoiDefinido();

		logger.info("Consultando artistas com base em exemplo: {}", exemplo);
		List<Artista> artistas = new ArrayList<>();
		List<Artista> artistasUniqueList = SetUniqueList.setUniqueList(artistas);

		logger.debug("Consultando no repositório local");
		artistasUniqueList.addAll(repositorio.consultarPor(exemplo));

		logger.debug("Consultando na web");
		artistasUniqueList.addAll(artistaWs.consultarPor(exemplo));

		logger.debug("Ordenando artistas");
		artistas.sort(new ArtistasPorNomeComparator());

		return artistasUniqueList;
	}

	private void verificarSeExemploFoiDefinido() {
		if (Objects.isNull(exemplo)) {
			throw new IllegalStateException("O artista de exemplo não foi informado");
		}
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao consultar artistas a partir do exemplo: {}", exemplo, exception);
			fireError(exception);
		}
	}

	public Artista getExemplo() {
		return exemplo;
	}

	public void setExemplo(Artista exemplo) {
		this.exemplo = exemplo;
	}
}
