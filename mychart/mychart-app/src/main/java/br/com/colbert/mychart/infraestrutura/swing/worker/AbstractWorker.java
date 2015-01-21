package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import br.com.colbert.mychart.ui.comum.messages.MessagesView;

/**
 * Classe base para todos os {@link SwingWorker}s da aplicação.
 * 
 * @author Thiago Colbert
 * @since 09/01/2015
 *
 * @param <T>
 *            the result type returned by this SwingWorker's doInBackground and get methods
 * @param <V>
 *            the type used for carrying out intermediate results by this SwingWorker's publish and process methods
 */
@Dependent
public abstract class AbstractWorker<T, V> extends SwingWorker<T, V> {

	@Inject
	protected MessagesView messagesView;

	protected boolean doneWithSuccess = true;
	protected String errorMessage;

	@Inject
	private WorkerWaitListener workerWaitListener;

	@PostConstruct
	protected void init() {
		addWorkerStateListener(workerWaitListener);
	}

	/**
	 * Notifica os ouvintes interessados de que ocorreu um erro durante a execução da tarefa.
	 * 
	 * @param exception
	 *            a exceção que ocorreu
	 */
	protected void fireError(Exception exception) {
		String rootCauseMessage = ExceptionUtils.getRootCauseMessage(exception);
		errorMessage = StringUtils.isNotBlank(rootCauseMessage) ? rootCauseMessage : exception.getLocalizedMessage();
		firePropertyChange("errorMessage", null, errorMessage);
		firePropertyChange("doneWithSuccess", doneWithSuccess, false);
		doneWithSuccess = false;
	}

	/**
	 * Adiciona um {@link WorkerStateListener}.
	 * 
	 * @param listener
	 */
	public void addWorkerStateListener(WorkerStateListener listener) {
		addPropertyChangeListener(listener);
	}

	/**
	 * Adiciona um {@link WorkerDoneListener}.
	 * 
	 * @param listener
	 */
	public void addWorkerDoneListener(WorkerDoneListener listener) {
		addPropertyChangeListener(listener);
	}

	/**
	 * Obtém o resultado da execução da tarefa. Este método só deve ser chamado após a tarefa ter sido finalizada, pois caso o
	 * método {@link #get()} lance alguma exceção, ela será relançada como uma {@link IllegalStateException}.
	 * 
	 * @return o resultado da execução
	 */
	public T getResult() {
		try {
			return get();
		} catch (InterruptedException | ExecutionException exception) {
			throw new IllegalStateException("Erro ao processar resultado", exception);
		}
	}

	public boolean isDoneWithSuccess() {
		return doneWithSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
