package br.com.colbert.mychart.aplicacao.artista;

import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;

/**
 * Executor de remoção de artistas.
 * 
 * @author Thiago Colbert
 * @since 16/01/2015
 */
public class RemoverArtistaWorker extends AbstractWorker<Boolean, Void> {

	@Inject
	private Logger logger;

	@Inject
	private ArtistaRepository repositorio;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	private String idArtista;

	@Override
	@Transactional
	protected Boolean doInBackground() throws Exception {
		verificarSeIdArtistaFoiDefinido();

		logger.info("Removendo artista com ID: {}", idArtista);
		return repositorio.remover(idArtista);
	}

	private void verificarSeIdArtistaFoiDefinido() {
		if (StringUtils.isBlank(idArtista)) {
			throw new IllegalStateException("ID do artista não definido");
		}
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao remover artista pelo ID: {}", idArtista, exception);
			fireError(exception);
		}
	}

	public String getIdArtista() {
		return idArtista;
	}

	public void setIdArtista(String idArtista) {
		this.idArtista = idArtista;
	}
}
