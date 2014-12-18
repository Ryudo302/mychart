package br.com.colbert.base.ui;

import java.awt.*;

import javax.swing.JButton;

/**
 * Fábrica de componentes botões.
 * 
 * @author Thiago Colbert
 * @since 12/12/2014
 */
public final class ButtonFactory {

	private static final Color COR_TEXTO_PADRAO_BOTOES = Color.BLACK;
	private static final Font FONTE_PADRAO_BOTOES = new Font("Tahoma", Font.BOLD, 11);

	private ButtonFactory() {

	}

	/**
	 * @wbp.factory
	 */
	public static JButton createJButton(String text, String toolTipText) {
		JButton button = new JButton(text);
		button.setToolTipText(toolTipText);
		button.setForeground(COR_TEXTO_PADRAO_BOTOES);
		button.setFont(FONTE_PADRAO_BOTOES);
		return button;
	}
}