package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.io.Serializable;

import javax.swing.SwingWorker;

/**
 * Classe abstrata que implementa a interface {@link WorkerDoneListener}, permitindo que as subclasses sobrescrevam apenas aqueles
 * métodos aos quais tem interesse.
 * 
 * @author Thiago Colbert
 * @since 20/01/2015
 */
public abstract class WorkerDoneAdapter implements WorkerDoneListener, Serializable {

	private static final long serialVersionUID = 739500686240271060L;

	@Override
	public void doneWithSuccess(SwingWorker<?, ?> worker) {
	}

	@Override
	public void doneWithError(SwingWorker<?, ?> worker, Throwable throwable) {
	}
}
