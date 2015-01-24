package br.com.colbert.mychart.infraestrutura.swing.worker;

import javax.swing.SwingWorker;

import br.com.colbert.base.ui.FormView;

/**
 * {@link WorkerDoneListener} que limpa a tela após a tarefa ser executada.
 * 
 * @author Thiago Colbert
 * @since 24/01/2015
 */
public class LimparTelaWorkerListener extends WorkerDoneAdapter {

	private static final long serialVersionUID = -5104660001094449780L;

	private final FormView<?> view;

	public LimparTelaWorkerListener(FormView<?> view) {
		this.view = view;
	}

	@Override
	public void doneWithSuccess(SwingWorker<?, ?> worker) {
		view.limparTela();
	}
}
