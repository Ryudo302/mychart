package br.com.colbert.mychart.ui.topmusical;

import br.com.colbert.base.ui.View;
import br.com.colbert.mychart.dominio.topmusical.TopMusicalConfiguration;

/**
 * Uma {@link View} de {@link TopMusicalConfiguration}.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
public interface TopMusicalConfigView extends View {

	/**
	 * Torna a janela visivel.
	 */
	void show();

	/**
	 * Fecha a janela.
	 */
	void close();
}
