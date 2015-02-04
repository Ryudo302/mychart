package br.com.colbert.mychart.aplicacao.topmusical;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.Presenter;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.topmusical.PrimeiroTopMusicalDialog;

/**
 * <em>Presenter</em> da configuração de um primeiro {@link TopMusical}.
 * 
 * @author Thiago Colbert
 * @since 04/02/2015
 */
@ApplicationScoped
public class PrimeiroTopMusicalPresenter implements Presenter, Serializable {

	private static final long serialVersionUID = 3714163864774692060L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;

	@Inject
	private TopMusicalFactory topMusicalFactory;

	@Inject
	private PrimeiroTopMusicalDialog view;
	@Inject
	private MessagesView messagesView;

	private Optional<TopMusical> topMusical;

	@PostConstruct
	@Override
	public void doBinding() {
		logger.trace("Definindo bindings");
		appController.bindPresenter(view, this);
	}

	@Override
	public void start() {
		logger.debug("Iniciando");
		view.show();
	}

	public Optional<TopMusical> getTopMusical() {
		return topMusical;
	}

	public void adicionarCancao() {

	}

	public void removerCancao() {

	}

	public void moveCancaoParaCima() {

	}

	public void moverCancaoParaBaixo() {

	}

	public void salvar() {
		List<Cancao> cancoes = view.getCancoes();
		Integer quantidadePosicoes = topMusicalFactory.getConfig().getQuantidadePosicoes();
		if (cancoes.size() == quantidadePosicoes) {
			topMusical = Optional.of(topMusicalFactory.novo(view.getDataInicial(), cancoes));
		} else {
			messagesView.adicionarMensagemAlerta("Devem ser adicionadas " + quantidadePosicoes + " canções!");
		}

		view.close();
	}

	public void cancelar() {
		topMusical = Optional.empty();
		view.close();
	}
}
