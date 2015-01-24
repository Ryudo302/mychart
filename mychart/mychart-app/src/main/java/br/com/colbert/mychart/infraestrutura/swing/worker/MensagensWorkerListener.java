package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingWorker;

import br.com.colbert.mychart.ui.comum.messages.MessagesView;

/**
 * {@link WorkerDoneListener} que exibe mensagem de sucesso ou erro, dependendo do resultado da execução da tarefa.
 * 
 * @author Thiago Colbert
 * @since 23/01/2015
 */
public class MensagensWorkerListener extends WorkerDoneAdapter {

	private static final long serialVersionUID = -2557192126542561121L;

	private final MessagesView messagesView;
	private final String mensagemSucesso;
	private final String mensagemErro;

	public MensagensWorkerListener(MessagesView messagesView, String mensagemSucesso, String mensagemErro) {
		this.messagesView = messagesView;
		this.mensagemSucesso = mensagemSucesso;
		this.mensagemErro = mensagemErro;
	}

	@Override
	public void doneWithSuccess(SwingWorker<?, ?> worker) {
		messagesView.adicionarMensagemSucesso(formatarMensagem(mensagemSucesso, worker));
	}

	private String formatarMensagem(String mensagem, SwingWorker<?, ?> worker) {
		if (mensagem.indexOf('{') != -1) {
			String metodo = mensagem.substring(mensagem.indexOf('{') + 1, mensagem.indexOf('}'));
			Object result = ((AbstractWorker<?, ?>) worker).getResult();
			return mensagem.replace('{' + metodo + '}', invokeMethod(result, metodo));
		} else {
			return mensagem;
		}
	}

	private String invokeMethod(Object result, String metodo) {
		try {
			return result.getClass().getMethod(metodo).invoke(result).toString();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException exception) {
			throw new IllegalArgumentException("Erro ao invocar método " + metodo + " do objeto " + result, exception);
		}
	}

	@Override
	public void doneWithError(SwingWorker<?, ?> worker, Throwable error) {
		messagesView.adicionarMensagemErro(mensagemErro, error);
	}
}
