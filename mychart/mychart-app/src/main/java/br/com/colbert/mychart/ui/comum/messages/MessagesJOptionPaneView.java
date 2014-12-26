package br.com.colbert.mychart.ui.comum.messages;

import java.text.MessageFormat;

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
	public void adicionarMensagemErro(String resumo, String detalhes) {
		JOptionPane.showMessageDialog(null, MessageFormat.format("{0}:\n\n{1}\n\n", resumo, detalhes), "Erro",
				JOptionPane.ERROR_MESSAGE);
	}
}
