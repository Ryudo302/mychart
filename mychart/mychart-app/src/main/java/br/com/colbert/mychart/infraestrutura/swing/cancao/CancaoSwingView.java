package br.com.colbert.mychart.infraestrutura.swing.cancao;

import java.util.Collection;

import javax.swing.JPanel;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.ui.cancao.CancaoView;

/**
 * Implementação de {@link CancaoView} utilizando um {@link JPanel}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
public class CancaoSwingView extends JPanel implements CancaoView {

	private static final long serialVersionUID = -3094420814151147640L;

	/**
	 * Create the panel.
	 */
	public CancaoSwingView() {

	}

	@Override
	public void setCancaoAtual(Cancao cancao) {
	}

	@Override
	public void setCancoes(Collection<Cancao> cancoes) {
	}
}
