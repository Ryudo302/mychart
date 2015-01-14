package br.com.colbert.mychart.aplicacao;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.SwingWorker;

import br.com.colbert.mychart.aplicacao.WorkerWaitListener;
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

	@Inject
	private WorkerWaitListener workerWaitListener;

	@PostConstruct
	protected void init() {
		addPropertyChangeListener(workerWaitListener);
	}
}
