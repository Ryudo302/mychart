package br.com.colbert.mychart.aplicacao.comum;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.enterprise.inject.Instance;
import javax.inject.*;
import javax.swing.SwingWorker;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.Presenter;
import br.com.colbert.mychart.aplicacao.principal.MainPresenter;
import br.com.colbert.mychart.infraestrutura.swing.worker.WorkerDoneAdapter;
import br.com.colbert.mychart.ui.comum.messages.*;

/**
 * <em>Presenter</em> da tela de erros.
 * 
 * @author Thiago Colbert
 * @since 24/01/2015
 */
@Singleton
public class ErroPresenter implements UncaughtExceptionHandler, Presenter, Serializable {

	private static final long serialVersionUID = -645388939150633119L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;

	@Inject
	private Instance<MainPresenter> mainPresenter;
	@Inject
	private ErroDialog view;

	@Inject
	private transient Instance<ReportarErroWorker> reportarErroWorker;

	private Erro erro;

	@Override
	public void doBinding() {
		appController.bind(view, erro, this);
	}

	@Override
	public void start() {
		logger.debug("Iniciando");
	}

	@Override
	public void uncaughtException(Thread thread, Throwable thrown) {
		logger.error("Erro não tratado (thread: {})", thread.getName(), thrown);

		erro = new Erro(thrown);
		doBinding();

		view.show();

		if (!mainPresenter.get().isJanelaVisivel()) {
			// ocorreu erro durante a inicialização
			System.exit(-1);
		}
	}

	public void ok() {
		view.close();
	}

	public void reportar() {
		logger.info("Reportando erro: {}", erro);
		ReportarErroWorker worker = reportarErroWorker.get();
		worker.setErro(erro);
		worker.execute();
		worker.addWorkerDoneListener(new WorkerDoneAdapter() {

			private static final long serialVersionUID = 7519112078156828539L;

			@Override
			public void doneWithSuccess(SwingWorker<?, ?> worker) {
				view.setNotificacaoHabilitada(false);
			}
		});
	}
}
