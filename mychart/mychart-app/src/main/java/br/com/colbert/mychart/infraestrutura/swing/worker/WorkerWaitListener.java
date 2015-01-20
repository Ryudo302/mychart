package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.awt.Window;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.SwingWorker.StateValue;

import org.slf4j.Logger;

import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;
import br.com.colbert.mychart.ui.comum.LoadingDialog;

/**
 * Listener responsável por exibir e esconder a {@link LoadingDialog} quando uma tarefa é executada em <em>background</em>.
 * 
 * @author Thiago Colbert
 * @since 09/01/2015
 */
@Dependent
public class WorkerWaitListener implements WorkerStateListener {

	@Inject
	private Logger logger;

	@Inject
	@LoadingView
	private Window loadingView;

	private boolean exibindo;

	@Override
	public void stateChange(SwingWorker<?, ?> source, StateValue oldState, StateValue newState) {
		logger.trace("Mudança de estado do worker {} de {} para {}", source, oldState, newState);
		switch (newState) {
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

	private void hideView() {
		if (exibindo) {
			logger.trace("Ocultando tela");
			SwingUtils.invokeLater(() -> loadingView.setVisible(false));
			exibindo = false;
		}
	}

	private void showView() {
		if (!exibindo) {
			logger.trace("Exibindo tela");
			SwingUtils.invokeLater(() -> loadingView.setVisible(true));
			exibindo = true;
		}
	}
}
