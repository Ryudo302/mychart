package br.com.colbert.mychart.aplicacao.cancao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.Presenter;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;
import br.com.colbert.mychart.ui.cancao.CancaoDialog;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.principal.PainelTelaPrincipal;

/**
 * <em>Presenter</em> de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 03/02/2015
 */
@ApplicationScoped
public class CancaoPresenter implements Presenter, Serializable {

	private static final long serialVersionUID = -8746099528166311584L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;

	@Inject
	@PainelTelaPrincipal
	private CancaoDialog view;
	@Inject
	private MessagesView messagesView;

	@Inject
	private Instance<ConsultarCancoesWorker> consultarCancoesWorker;

	@PostConstruct
	@Override
	public void doBinding() {
		logger.trace("Definindo bindings");
		appController.bind(view, Cancao.CANCAO_NULL.clone(), this);
	}

	@Override
	public void start() {
		logger.debug("Iniciando");
	}

	public List<Cancao> getCancoesSelecionadas() {
		return view.getCancoesSelecionadas();
	}

	public void consultarCancoes() {
		logger.info("Consultando artistas");
		String tituloCancao = view.getTituloCancao();
		if (StringUtils.isBlank(tituloCancao)) {
			messagesView.adicionarMensagemAlerta("Informe um nome a ser consultado.");
		} else {
			ConsultarCancoesWorker worker = consultarCancoesWorker.get();
			worker.setExemplo(new Cancao(tituloCancao));
			worker.execute();
			worker.addWorkerDoneListener(new DefinirConteudoTabelaWorkerListener<>(view));
			worker.addWorkerDoneListener(new MensagensWorkerListener(messagesView, "Foi(ram) encontrada(s) {size} canção(ões)",
					"Erro ao consultar canções"));
		}
	}

	public void selecionarCancoes() {
		view.close();
	}
}
