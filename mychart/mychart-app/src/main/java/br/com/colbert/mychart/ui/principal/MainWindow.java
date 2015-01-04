package br.com.colbert.mychart.ui.principal;

import br.com.colbert.base.ui.View;

/**
 * A tela principal da aplicação.
 * 
 * @author Thiago Colbert
 * @since 17/12/2014
 */
public interface MainWindow extends View {

	/**
	 * Torna a janela visivel.
	 */
	void show();

	/**
	 * Fecha a janela.
	 */
	void close();
}
