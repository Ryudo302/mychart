package br.com.colbert.mychart.aplicacao.comum;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.erro.ErroReporter;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;
import br.com.colbert.mychart.ui.comum.messages.Erro;

/**
 * Executor de notificação de erros na aplicação para os desenvolvedores.
 * 
 * @author Thiago Colbert
 * @since 01/02/2015
 */
public class ReportarErroWorker extends AbstractWorker<Void, Void> {

	@Inject
	private Logger logger;

	@Inject
	private ErroReporter erroReporter;

	private Erro erro;

	@Override
	protected Void doInBackground() throws Exception {
		logger.debug("Reportando erro: {}", erro);
		erroReporter.reportar(erro);
		return null;
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Falha ao enviar notificação de erro: " + erro.getMensagem(), exception);
			fireError(exception);
		}
	}

	public Erro getErro() {
		return erro;
	}

	public void setErro(Erro erro) {
		this.erro = erro;
	}
}
