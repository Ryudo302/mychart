package br.com.colbert.mychart.aplicacao.topmusical;

import java.awt.event.ItemEvent;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.event.ChangeEvent;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.topmusical.TopMusicalConfiguration;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.topmusical.TopMusicalConfigDialog;

/**
 * <em>Presenter</em> de {@link TopMusicalConfiguration}.
 * 
 * @author Thiago Colbert
 * @since 29/01/2015
 */
public class TopMusicalConfigPresenter implements Serializable {

	private static final long serialVersionUID = 8549455585957411436L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;

	@Inject
	private TopMusicalConfiguration topMusicalConfiguration;

	@Inject
	private TopMusicalConfigDialog view;
	@Inject
	private MessagesView messagesView;

	@PostConstruct
	protected void doBinding() {
		logger.trace("Definindo bindings");
		appController.bind(view, topMusicalConfiguration, this);
	}

	public void start() {
		logger.debug("Iniciando");
		view.getFrequenciaComboBox().setSelectedItem(topMusicalConfiguration.getFrequencia());
		view.getQuantidadePosicoesSpinner().setValue(topMusicalConfiguration.getQuantidadePosicoes());
		view.show();
	}

	public void quantidadePosicoesSelecionada(ChangeEvent evento) {
		topMusicalConfiguration.setQuantidadePosicoes(view.getQuantidadePosicoes());
	}

	public void frequenciaSelecionada(ItemEvent evento) {
		topMusicalConfiguration.setFrequencia(view.getFrequencia());
	}

	public void salvarAlteracoes() {
		try {
			// TODO Criar Worker
			topMusicalConfiguration.salvar();
			view.close();
		} catch (IOException exception) {
			logger.error("Erro ao salvar configurações", exception);
			messagesView.adicionarMensagemErro("Erro ao salvar configurações", exception);
		}
	}

	public void descartarAlteracoes() {
		try {
			// TODO Criar Worker
			topMusicalConfiguration.descartarAlteracoes();
			view.close();
		} catch (Exception exception) {
			logger.warn("Erro ao desfazer alterações", exception);
		}
	}
}
