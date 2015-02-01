package br.com.colbert.mychart.aplicacao.comum;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.enterprise.inject.Instance;
import javax.inject.*;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.mychart.aplicacao.principal.MainPresenter;
import br.com.colbert.mychart.ui.comum.messages.*;

/**
 * 
 * @author Thiago Colbert
 * @since 24/01/2015
 */
@Singleton
public class ErroPresenter implements UncaughtExceptionHandler, Serializable {

	private static final long serialVersionUID = -645388939150633119L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;

	//@Inject
	private MainPresenter mainPresenter;
	@Inject
	private ErroDialog view;

	@Inject
	private transient Instance<ReportarErroWorker> reportarErroWorker;

	private Erro erro;

	public void start() {
		logger.debug("Iniciando");
	}

	@Override
	public void uncaughtException(Thread thread, Throwable thrown) {
		logger.error("Erro não tratado (thread: {})", thread.getName(), thrown);
		erro = new Erro(thrown);
		appController.bind(view, erro, this);
		view.show();

		// TODO
		/*if (!mainPresenter.isJanelaVisivel()) {
			// ocorreu erro durante a inicialização
			System.exit(-1);
		}*/
	}

	public void ok() {
		view.close();
	}

	public void reportar() {
		logger.info("Reportando erro: {}", erro);
		ReportarErroWorker worker = reportarErroWorker.get();
		worker.setErro(erro);
		worker.execute();
	}
}
