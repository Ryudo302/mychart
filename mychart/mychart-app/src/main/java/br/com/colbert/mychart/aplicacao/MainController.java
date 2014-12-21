package br.com.colbert.mychart.aplicacao;

import java.awt.EventQueue;
import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;

import br.com.colbert.mychart.infraestrutura.eventos.app.*;
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

	/**
	 * Inicia a aplicação.
	 */
	public void iniciar(@Observes ContainerInitialized event) {
		EventQueue.invokeLater(() -> {
			logger.info("Iniciando...");
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
		EventQueue.invokeLater(() -> {
			logger.info("Encerrando...");
			mainWindow.close();
			System.exit(0);
		});
	}
}
