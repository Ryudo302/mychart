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
			doneWithError(worker, worker.getLastError());
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
	 * @param error
	 *            o erro que ocorreu
	 */
	void doneWithError(SwingWorker<?, ?> worker, Throwable error);
}
