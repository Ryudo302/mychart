package br.com.colbert.mychart.infraestrutura.swing.worker;

import javax.swing.*;
import javax.swing.SwingWorker.StateValue;

/**
 * Extensão de {@link WorkerStateListener} que permite verificar se a tarefa foi finalizada com sucesso ou com erros.
 * 
 * @author Thiago Colbert
 * @since 20/01/2015
 */
public interface WorkerDoneListener extends WorkerStateListener {

	@Override
	default void stateChange(SwingWorker<?, ?> source, StateValue oldState, StateValue newState) {
		AbstractWorker<?, ?> worker = (AbstractWorker<?, ?>) source;
		if (newState == StateValue.DONE && worker.isDoneWithSuccess()) {
			doneWithSuccess(worker);
		} else if (newState == StateValue.DONE) {
			doneWithError(worker, worker.getErrorMessage());
		}
	}

	/**
	 * A tarefa foi finalizada com sucesso.
	 * 
	 * @param worker
	 *            a tarefa
	 */
	void doneWithSuccess(SwingWorker<?, ?> worker);

	/**
	 * A tarefa foi finalizada com erro.
	 * 
	 * @param worker
	 *            a tarefa
	 * @param errorMessage
	 *            a mensagem de erro a ser exibida
	 */
	void doneWithError(SwingWorker<?, ?> worker, String errorMessage);
}
