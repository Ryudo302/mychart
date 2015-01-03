package br.com.colbert.mychart.ui.principal;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.swing.*;

/**
 * Painel representando a tela inicial da aplicação.
 * 
 * @author Thiago Colbert
 * @since 20/12/2014
 *
 */
@ApplicationScoped
public class InicioPanel implements Serializable {

	private static final long serialVersionUID = -4417535149698487678L;

	private JPanel panel;

	private JLabel nomeAppLabel;

	public static void main(String[] args) {
		new InicioPanel().init();
	}

	@PostConstruct
	protected void init() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		nomeAppLabel = new JLabel();
		panel.add(nomeAppLabel);
	}

	public JPanel getPanel() {
		return panel;
	}
}
