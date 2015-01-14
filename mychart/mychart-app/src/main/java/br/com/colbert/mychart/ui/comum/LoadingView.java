package br.com.colbert.mychart.ui.comum;

import br.com.colbert.base.ui.View;

/**
 * {@link View} exibida quando é aguardado o processamento de uma tarefa em <em>background</em>.
 * 
 * @author Thiago Colbert
 * @since 09/01/2015
 */
public interface LoadingView extends View {

	/**
	 * Exibe a tela.
	 */
	void show();

	/**
	 * Esconde a tela.
	 */
	void hide();
}
