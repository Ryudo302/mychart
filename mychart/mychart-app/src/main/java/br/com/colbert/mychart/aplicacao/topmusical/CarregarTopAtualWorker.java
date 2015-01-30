package br.com.colbert.mychart.aplicacao.topmusical;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.dominio.topmusical.repository.TopMusicalRepository;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;

/**
 * Executor que carrega o top musical atual.
 * 
 * @author Thiago Colbert
 * @since 30/01/2015
 */
public class CarregarTopAtualWorker extends AbstractWorker<Optional<TopMusical>, Void> {

	@Inject
	private Logger logger;

	@Inject
	private TopMusicalRepository repositorio;

	@Override
	protected Optional<TopMusical> doInBackground() throws Exception {
		logger.debug("Consultando no repositório");
		return repositorio.consultarAtual();
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao consultar top atual", exception);
			fireError(exception);
		}
	}
}
