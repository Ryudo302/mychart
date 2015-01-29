package br.com.colbert.mychart.aplicacao.principal;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.mychart.aplicacao.artista.ArtistaPresenter;
import br.com.colbert.mychart.aplicacao.comum.ErroPresenter;
import br.com.colbert.mychart.aplicacao.topmusical.TopMusicalConfigPresenter;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.ui.comum.messages.*;
import br.com.colbert.mychart.ui.principal.MainWindow;
import br.com.colbert.mychart.ui.sobre.SobreDialog;
import br.com.colbert.mychart.ui.topmusical.TopMusicalConfigDialog;

/**
 * <em>Presenter</em> principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 18/12/2014
 */
public class MainPresenter implements Serializable {

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
	private ErroPresenter erroPresenter;
	@Inject
	private TopMusicalConfigDialog topMusicalConfigDialog;
	@Inject
	private SobreDialog sobreDialog;

	@Inject
	private ArtistaPresenter artistaPresenter;
	@Inject
	private TopMusicalConfigPresenter topMusicalConfigPresenter;

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
		Thread.setDefaultUncaughtExceptionHandler(erroPresenter);

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

	/**
	 * Verifica se a janela principal está visível ou não.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean isJanelaVisivel() {
		return mainWindow.getFrame().isVisible();
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
		topMusicalConfigPresenter.start();
	}

	public void exibirTelaSobre() {
		logger.debug("Exibindo janela 'Sobre'");
		sobreDialog.setVisible(true);
	}
}
