package br.com.colbert.mychart.aplicacao;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.infraestrutura.eventos.app.*;
import br.com.colbert.mychart.ui.MainWindow;

/**
 * Controlador principal da aplicação.
 * 
 * @author Thiago Colbert
 * @since 18/12/2014
 */
public class MainController {

	@Inject
	private Logger logger;

	@Inject
	private MainWindow mainWindow;

	/**
	 * Inicia a aplicação.
	 */
	public void iniciar() {
		logger.info("Iniciando");
		mainWindow.show();
	}

	/**
	 * Encerra a aplicação.
	 * 
	 * @param window
	 *            a janela principal da aplicação
	 */
	public void sair(@Observes @StatusAplicacao(TipoStatusAplicacao.ENCERRADA) MainWindow window) {
		logger.info("Encerrando...");
		mainWindow.close();
	}
}
