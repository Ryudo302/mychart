package br.com.colbert.mychart.infraestrutura.swing.worker;

import javax.swing.*;
import javax.swing.SwingWorker.StateValue;

/**
 * TODO
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
	 * 
	 * @param worker
	 */
	void doneWithSuccess(SwingWorker<?, ?> worker);

	/**
	 * 
	 * @param worker
	 */
	void doneWithError(SwingWorker<?, ?> worker, String errorMessage);
}
