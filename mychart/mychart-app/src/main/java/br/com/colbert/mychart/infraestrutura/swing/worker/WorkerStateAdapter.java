package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.io.Serializable;

import javax.swing.SwingWorker.StateValue;

/**
 * Implementação de {@link WorkerStateListener} que separa cada status do <em>worker</em> em um método diferente.
 * 
 * @author Thiago Colbert
 * @since 16/01/2015
 */
public abstract class WorkerStateAdapter implements WorkerStateListener, Serializable {

	private static final long serialVersionUID = -8509761758928914385L;

	@Override
	public void stateChange(StateValue oldState, StateValue newState) {
		switch (newState) {
		case STARTED:
			started();
			break;
		case PENDING:
			pending();
			break;
		case DONE:
			done();
			break;
		default:
			break;
		}
	}

	/**
	 * @see StateValue#PENDING
	 */
	public void pending() {

	}

	/**
	 * @see StateValue#STARTED
	 */
	public void started() {

	}

	/**
	 * @see StateValue#DONE
	 */
	public void done() {

	}
}
