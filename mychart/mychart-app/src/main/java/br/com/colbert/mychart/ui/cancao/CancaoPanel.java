package br.com.colbert.mychart.ui.cancao;

import java.util.Collection;

import javax.swing.JPanel;

import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Implementação de {@link CancaoView} utilizando um {@link JPanel}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
public class CancaoPanel extends JPanel implements CancaoView {

	private static final long serialVersionUID = -3094420814151147640L;

	/**
	 * Create the panel.
	 */
	public CancaoPanel() {

	}

	@Override
	public void setCancaoAtual(Cancao cancao) {
	}

	@Override
	public void setCancoes(Collection<Cancao> cancoes) {
	}
}
