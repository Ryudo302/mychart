package br.com.colbert.mychart.ui.principal;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

/**
 * Painel representando a tela inicial da aplicação.
 * 
 * @author Thiago Colbert
 * @since 20/12/2014
 *
 */
public class InicioPanel extends JPanel {

	private static final long serialVersionUID = -4417535149698487678L;
	
	private JLabel nomeAppLabel;

	/**
	 * Create the panel.
	 */
	public InicioPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		nomeAppLabel = new JLabel();
		add(nomeAppLabel);
	}
}
