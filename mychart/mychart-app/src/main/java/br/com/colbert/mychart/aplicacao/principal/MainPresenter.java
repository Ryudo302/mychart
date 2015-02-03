package br.com.colbert.mychart.aplicacao.principal;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.mvp4j.AppController;
import org.mvp4j.utils.MvpUtils;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.Presenter;
import br.com.colbert.base.ui.View;
import br.com.colbert.mychart.aplicacao.comum.ErroPresenter;
import br.com.colbert.mychart.aplicacao.topmusical.TopMusicalConfigPresenter;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.ui.artista.ArtistaPanel;
import br.com.colbert.mychart.ui.comum.messages.*;
import br.com.colbert.mychart.ui.inicio.InicioPanel;
import br.com.colbert.mychart.ui.principal.*;
import br.com.colbert.mychart.ui.sobre.SobreDialog;
import br.com.colbert.mychart.ui.topmusical.TopMusicalPanel;

/**
 * <em>Presenter</em> principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 18/12/2014
 */
public class MainPresenter implements Presenter, Serializable {

	private static final long serialVersionUID = 9104572255370820023L;

	@Inject
	private Logger logger;

	@Inject
	private AppController appController;

	@Inject
	private MainWindow mainWindow;
	@Inject
	private MessagesView messagesView;
	@Inject
	private SobreDialog sobreDialog;

	@Inject
	@PainelTelaPrincipal
	private Instance<View> paineis;
	@Inject
	@Any
	private Instance<Presenter> presenters;

	@Inject
	@TituloAplicacao
	private String tituloAplicacao;

	@Inject
	private ErroPresenter erroPresenter;

	@Inject
	private EntityManagerFactory entityManagerFactory;
	@Inject
	private LastFmWs lastFmWs;

	@PostConstruct
	@Override
	public void doBinding() {
		appController.bindPresenter(mainWindow, this);
	}

	/**
	 * Método invocado assim que o contexto CDI é inicializado.
	 * 
	 * @param event
	 *            o evento
	 */
	protected void contextoInicializado(@Observes ContainerInitialized event) {
		start();
	}

	@Override
	public void start() {
		logger.info("Iniciando...");
		Thread.setDefaultUncaughtExceptionHandler(erroPresenter);
		entityManagerFactory.toString();

		if (!lastFmWs.ping()) {
			messagesView
					.adicionarMensagemAlerta("Não foi possível acessar os serviços da LastFM. Verifique sua conexão com a internet e também se o site http://www.lastfm.com.br está respondendo.");
		}

		setUpView();

		logger.debug("Exibindo a janela principal");
		mainWindow.show();
	}

	private void setUpView() {
		paineis.forEach(painel -> {
			logger.debug("Adicionando à tela principal painel sob o nome '{}': {}", painel.getName(), painel);
			mainWindow.getContainer().add(painel.getContainer(), painel.getName());
		});

		logger.debug("Título da aplicação: {}", tituloAplicacao);
		mainWindow.setTitulo(tituloAplicacao);
	}

	/**
	 * Encerra a aplicação.
	 */
	public void sair() {
		if (messagesView.exibirConfirmacao("Deseja realmente sair?") == RespostaConfirmacao.SIM) {
			logger.info("Encerrando...");
			mainWindow.close();
		}
	}

	/**
	 * Verifica se a janela principal está visível ou não.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean isJanelaVisivel() {
		return mainWindow.getFrame().isVisible();
	}

	public void exibirTelaInicio() {
		mudarConteudoTela(InicioPanel.class);
	}

	public void exibirTelaArtistas() {
		mudarConteudoTela(ArtistaPanel.class);
	}

	public void exibirTelaTopPrincipal() {
		mudarConteudoTela(TopMusicalPanel.class);
	}

	public void exibirTelaConfiguracoes() {
		presenters.select(TopMusicalConfigPresenter.class).get().start();
	}

	public void exibirTelaSobre() {
		sobreDialog.show();
	}

	private void mudarConteudoTela(Class<? extends View> novaTela) {
		View tela = paineis.select(novaTela, novaTela.getAnnotation(PainelTelaPrincipal.class)).get();
		Class<Presenter> presenterClass = MvpUtils.getPresenterClass(tela.getClass());
		logger.debug("Exibindo tela: " + tela.getName());
		mainWindow.mudarTela(tela.getName());
		presenters.select(presenterClass).get().start();
	}
}
