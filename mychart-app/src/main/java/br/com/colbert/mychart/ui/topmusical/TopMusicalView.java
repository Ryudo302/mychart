package br.com.colbert.mychart.ui.topmusical;

import br.com.colbert.mychart.dominio.topmusical.TopMusical;

/**
 * Uma view de {@link TopMusical}.
 * 
 * @author Thiago Colbert
 * @since 09/12/2014
 */
public interface TopMusicalView {

	/**
	 * Obtém o {@link TopMusical} sendo atualmente exibido pela visão.
	 * 
	 * @return o top musical sendo exibido
	 */
	TopMusical getTopMusical();

	/**
	 * Define o {@link TopMusical} sendo atualmente exibido pela visão.
	 * 
	 * @param topMusical
	 *            o top musical
	 */
	void setTopMusical(TopMusical topMusical);
}
