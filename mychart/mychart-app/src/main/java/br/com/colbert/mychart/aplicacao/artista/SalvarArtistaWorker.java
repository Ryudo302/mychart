package br.com.colbert.mychart.aplicacao.artista;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;

/**
 * Executor de salvamento de artistas.
 * 
 * @author Thiago Colbert
 * @since 14/01/2015
 */
public class SalvarArtistaWorker extends AbstractWorker<Artista, Void> {

	@Inject
	private Logger logger;

	@Inject
	private ArtistaRepository repositorio;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	private Artista artista;

	@Override
	@Transactional
	protected Artista doInBackground() throws Exception {
		verificarSeArtistaFoiDefinido();

		logger.info("Salvando: {}", artista);
		return WorkflowEntidade.novo(artista, repositorio).validar(validadores).incluirOuAlterar(artista);
	}

	private void verificarSeArtistaFoiDefinido() {
		if (Objects.isNull(artista)) {
			throw new IllegalStateException("O artista não foi informado");
		}
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao salvar artista: " + artista, exception);
			fireError(exception);
		}
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}
}
