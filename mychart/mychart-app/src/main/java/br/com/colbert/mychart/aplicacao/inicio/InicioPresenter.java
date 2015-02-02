package br.com.colbert.mychart.aplicacao.inicio;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.Presenter;
import br.com.colbert.mychart.infraestrutura.info.*;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao.Formato;
import br.com.colbert.mychart.ui.inicio.InicioPanel;
import br.com.colbert.mychart.ui.principal.PainelTelaPrincipal;

/**
 * <em>Presenter</em> da tela inicial.
 * 
 * @author Thiago Colbert
 * @since 02/02/2015
 */
@ApplicationScoped
public class InicioPresenter implements Presenter, Serializable {

	private static final long serialVersionUID = 723542710035846939L;

	@Inject
	private Logger logger;
	@Inject
	private AppController appController;

	@Inject
	@TituloAplicacao(Formato.APENAS_NOME)
	private String tituloAplicacao;

	@Inject
	@PainelTelaPrincipal
	private InicioPanel view;

	@Override
	public void doBinding() {
		appController.bindPresenter(view, this);
	}

	@Override
	public void start() {
		logger.debug("Iniciando");
		view.setTexto(tituloAplicacao);
	}
}
