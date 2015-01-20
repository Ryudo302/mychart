package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.beans.*;

import javax.swing.SwingWorker;

import org.apache.commons.lang3.*;

/**
 * TODO
 * 
 * @author Thiago Colbert
 * @since 20/01/2015
 */
public interface WorkerDoneListener extends PropertyChangeListener {

	@Override
	default void propertyChange(PropertyChangeEvent event) {
		if (StringUtils.equals(event.getPropertyName(), "doneWithSuccess")) {
			AbstractWorker<?, ?> source = (AbstractWorker<?, ?>) event.getSource();
			if (BooleanUtils.isTrue((Boolean) event.getNewValue())) {
				doneWithSuccess(source);
			} else {
				doneWithError(source, source.getErrorMessage());
			}
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
