package br.com.colbert.mychart.aplicacao.topmusical;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.dominio.topmusical.repository.TopMusicalRepository;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;

/**
 * Executor de salvamento de tops musicais.
 * 
 * @author Thiago Colbert
 * @since 03/03/2015
 */
public class SalvarTopMusicalWorker extends AbstractWorker<TopMusical, Void> {

	@Inject
	private Logger logger;

	@Inject
	private TopMusicalRepository repositorio;

	@Inject
	@Any
	private Instance<Validador<TopMusical>> validadores;

	private TopMusical topMusical;

	@Override
	@Transactional
	protected TopMusical doInBackground() throws Exception {
		verificarSeTopMusicalFoiDefinido();

		logger.info("Salvando: {}", topMusical);
		return WorkflowEntidade.novo(topMusical, repositorio).validar(validadores).incluirOuAlterar(topMusical);
	}

	private void verificarSeTopMusicalFoiDefinido() {
		if (Objects.isNull(topMusical)) {
			throw new IllegalStateException("O top não foi informado");
		}
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao salvar top: " + topMusical, exception);
			fireError(exception);
		}
	}

	public TopMusical getTopMusical() {
		return topMusical;
	}

	public void setTopMusical(TopMusical topMusical) {
		this.topMusical = topMusical;
	}
}
