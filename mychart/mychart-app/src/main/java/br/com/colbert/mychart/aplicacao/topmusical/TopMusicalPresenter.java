package br.com.colbert.mychart.aplicacao.topmusical;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.topmusical.TopMusicalView;

/**
 * <em>Presenter</em> de {@link TopMusical}.
 * 
 * @author Thiago Colbert
 * @since 29/01/2015
 */
public class TopMusicalPresenter implements Serializable {

	private static final long serialVersionUID = 7945528859962384121L;

	@Inject
	private Logger logger;
	@Inject
	private AppController appController;

	@Inject
	private TopMusicalView view;
	@Inject
	private MessagesView messagesView;

	@PostConstruct
	protected void doBinding() {
		logger.trace("Definindo bindings");
		appController.bindPresenter(view, this);
	}

	public void start() {
		logger.debug("Iniciando");
	}
}
