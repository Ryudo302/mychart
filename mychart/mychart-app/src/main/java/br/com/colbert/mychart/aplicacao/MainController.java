package br.com.colbert.mychart.aplicacao;

import javax.enterprise.event.Observes;

import br.com.colbert.mychart.infraestrutura.CdiUtils;
import br.com.colbert.mychart.infraestrutura.eventos.app.*;
import br.com.colbert.mychart.ui.MainWindow;

/**
 * Controlador principal da aplicação.
 * 
 * @author Thiago Colbert
 * @since 18/12/2014
 */
public class MainController {

	/**
	 * Inicia a aplicação.
	 */
	public void iniciar() {
		CdiUtils.init();
		CdiUtils.getBean(MainWindow.class).show();
	}

	/**
	 * Encerra a aplicação.
	 * 
	 * @param window
	 *            a janela principal da aplicação
	 */
	public void sair(@Observes @StatusAplicacao(TipoStatusAplicacao.ENCERRADA) MainWindow window) {
		// TODO
		CdiUtils.shutdown();
	}
}
