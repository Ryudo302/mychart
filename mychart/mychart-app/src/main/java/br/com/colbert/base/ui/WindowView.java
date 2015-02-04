package br.com.colbert.base.ui;

import java.awt.*;

import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;

/**
 * Um tipo de {@link View} que é representada em uma janela que pode ser, dentre outras, aberta e fechada.
 * 
 * @author Thiago Colbert
 * @since 02/02/2015
 */
public interface WindowView extends View {

	/**
	 * Torna a janela visível.
	 */
	default void show() {
		SwingUtils.invokeLater(() -> getContainer().setVisible(true));
	}

	/**
	 * Fecha a janela.
	 */
	default void close() {
		SwingUtils.invokeLater(() -> getContainer().setVisible(false));
	}

	/**
	 * Verifica se a janela está visível.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	default boolean isVisible() {
		return getContainer().isVisible();
	}

	/**
	 * Altera o título da janela.
	 * 
	 * @param titulo
	 *            o novo título
	 */
	default void setTitulo(String titulo) {
		Container container = getContainer();
		if (container instanceof Frame) {
			((Frame) container).setTitle(titulo);
		} else if (container instanceof Dialog) {
			((Dialog) container).setTitle(titulo);
		}
	}
}
