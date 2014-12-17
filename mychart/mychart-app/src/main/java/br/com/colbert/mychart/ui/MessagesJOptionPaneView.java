package br.com.colbert.mychart.ui;

import javax.swing.JOptionPane;

/**
 * Implementação de {@link MessagesView} que utiliza {@link JOptionPane}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class MessagesJOptionPaneView implements MessagesView {

	@Override
	public void adicionarMensagemSucesso(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void adicionarMensagemAlerta(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "Alerta", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void adicionarMensagemErro(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
	}
}
