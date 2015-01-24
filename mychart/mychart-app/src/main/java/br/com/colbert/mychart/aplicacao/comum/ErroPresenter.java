package br.com.colbert.mychart.aplicacao.comum;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.mychart.aplicacao.principal.MainPresenter;
import br.com.colbert.mychart.ui.comum.messages.*;

/**
 * 
 * @author Thiago Colbert
 * @since 24/01/2015
 */
@ApplicationScoped
public class ErroPresenter implements UncaughtExceptionHandler, Serializable {

	private static final long serialVersionUID = -645388939150633119L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;

	@Inject
	private MainPresenter mainPresenter;
	@Inject
	private ErroDialog view;

	public void start() {
		logger.debug("Iniciando");
	}

	@Override
	public void uncaughtException(Thread thread, Throwable thrown) {
		logger.error("Erro não tratado (thread: {})", thread.getName(), thrown);
		appController.bind(view, new Erro(thrown), this);
		view.show();

		if (!mainPresenter.isJanelaVisivel()) {
			// ocorreu erro durante a inicialização
			System.exit(-1);
		}
	}
}
