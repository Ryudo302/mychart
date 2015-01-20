package br.com.colbert.mychart.aplicacao.principal;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.mychart.aplicacao.artista.ArtistaPresenter;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.ui.comum.messages.*;
import br.com.colbert.mychart.ui.principal.MainWindow;
import br.com.colbert.mychart.ui.sobre.SobreDialog;
import br.com.colbert.mychart.ui.topmusical.TopMusicalConfigView;

/**
 * <em>Presenter</em> principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 18/12/2014
 */
public class MainPresenter implements UncaughtExceptionHandler, Serializable {

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
	private TopMusicalConfigView topMusicalConfigView;
	@Inject
	private SobreDialog sobreDialog;

	@Inject
	private ArtistaPresenter artistaPresenter;

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@Inject
	private LastFmWs lastFmWs;

	@PostConstruct
	protected void doBinding() {
		appController.bindPresenter(mainWindow, this);
	}

	/**
	 * Inicia a aplicação.
	 */
	public void iniciar(@Observes ContainerInitialized event) {
		logger.info("Iniciando...");
		Thread.setDefaultUncaughtExceptionHandler(this);

		entityManagerFactory.toString();

		if (!lastFmWs.ping()) {
			messagesView
					.adicionarMensagemAlerta("Não foi possível acessar os serviços da LastFM. Verifique sua conexão com a internet e também se o site http://www.lastfm.com.br está respondendo.");
		}

		mainWindow.show();
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

	public void exibirTelaInicio() {
		logger.debug("Exibindo tela inicial");
		mainWindow.mudarTela(MainWindow.TELA_INICIAL);
	}

	public void exibirTelaArtistas() {
		logger.debug("Exibindo tela de artistas");
		mainWindow.mudarTela(MainWindow.TELA_ARTISTAS);
		artistaPresenter.start();
	}

	public void exibirTelaTopPrincipal() {
		logger.debug("Exibindo tela do top principal");
		mainWindow.mudarTela(MainWindow.TELA_TOP_PRINCIPAL);
	}

	public void exibirTelaConfiguracoes() {
		logger.debug("Exibindo tela de configurações");
		topMusicalConfigView.show();
	}

	public void exibirTelaSobre() {
		logger.debug("Exibindo janela 'Sobre'");
		sobreDialog.setVisible(true);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable thrown) {
		logger.error("Erro não tratado (thread: {})", thread.getName(), thrown);
		messagesView.adicionarMensagemErro("Ocorreu um erro não tratado", thrown.getLocalizedMessage());
	}
}
