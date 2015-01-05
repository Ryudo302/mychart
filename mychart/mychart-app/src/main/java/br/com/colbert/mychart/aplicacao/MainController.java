package br.com.colbert.mychart.aplicacao;

import java.awt.EventQueue;
import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;

import br.com.colbert.mychart.infraestrutura.eventos.app.*;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.ui.comum.messages.*;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * Controlador principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 18/12/2014
 */
public class MainController implements Serializable {

	private static final long serialVersionUID = 9104572255370820023L;

	@Inject
	private Logger logger;

	@Inject
	private MainWindow mainWindow;
	@Inject
	private MessagesView messagesView;

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@Inject
	private LastFmWs lastFmWs;

	/**
	 * Inicia a aplicação.
	 */
	public void iniciar(@Observes ContainerInitialized event) {
		logger.info("Iniciando...");
		entityManagerFactory.toString();

		if (!lastFmWs.ping()) {
			messagesView
					.adicionarMensagemAlerta("Não foi possível acessar os serviços da LastFM. Verifique sua conexão com a internet e também se o site http://www.lastfm.com.br está respondendo.");
		}

		EventQueue.invokeLater(() -> {
			mainWindow.show();
		});
	}

	/**
	 * Encerra a aplicação.
	 *
	 * @param window
	 *            a janela principal da aplicação
	 */
	public void sair(@Observes @StatusAplicacao(TipoStatusAplicacao.ENCERRADA) MainWindow window) {
		if (messagesView.exibirConfirmacao("Deseja realmente sair?") == RespostaConfirmacao.SIM) {
			logger.info("Encerrando...");

			EventQueue.invokeLater(() -> {
				mainWindow.close();
				System.exit(0);
			});
		}
	}
}
