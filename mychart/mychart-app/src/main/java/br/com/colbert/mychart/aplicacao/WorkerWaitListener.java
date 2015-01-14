package br.com.colbert.mychart.aplicacao;

import java.beans.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.SwingWorker.StateValue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.mychart.ui.comum.LoadingView;

/**
 * Listener responsável por exibir e esconder a {@link LoadingView} quando uma tarefa é executada em <em>background</em>.
 * 
 * @author Thiago Colbert
 * @since 09/01/2015
 */
@Dependent
public class WorkerWaitListener implements PropertyChangeListener {

	@Inject
	private Logger logger;
	@Inject
	private LoadingView loadingView;

	private boolean exibindo;

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (StringUtils.equals(event.getPropertyName(), "state")) {
			StateValue state = (StateValue) event.getNewValue();
			logger.debug("Estado atual = {}", state);

			switch (state) {
			case STARTED:
				showView();
				break;
			case DONE:
				hideView();
				break;
			default:
				break;
			}
		}
	}

	private void hideView() {
		if (exibindo) {
			logger.debug("Ocultando tela");
			loadingView.hide();
			exibindo = false;
		}
	}

	private void showView() {
		if (!exibindo) {
			logger.debug("Exibindo tela");
			loadingView.show();
			exibindo = true;
		}
	}
}
