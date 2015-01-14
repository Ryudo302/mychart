package br.com.colbert.mychart.infraestrutura.swing.comum;

import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.JOptionPane;

import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;
import br.com.colbert.mychart.ui.comum.messages.*;

/**
 * Implementação de {@link MessagesView} que utiliza {@link JOptionPane}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@ApplicationScoped
public class MessagesSwingView implements MessagesView {

	@Override
	public void adicionarMensagemSucesso(String mensagem) {
		SwingUtils.invokeLater(() -> JOptionPane.showMessageDialog(null, mensagem, "Info", JOptionPane.INFORMATION_MESSAGE));
	}

	@Override
	public void adicionarMensagemAlerta(String mensagem) {
		SwingUtils.invokeLater(() -> JOptionPane.showMessageDialog(null, mensagem, "Alerta", JOptionPane.WARNING_MESSAGE));
	}

	@Override
	public void adicionarMensagemErro(String resumo, String detalhes) {
		SwingUtils.invokeLater(() -> JOptionPane.showMessageDialog(null,
				MessageFormat.format("{0}:\n\n{1}\n\n", resumo, detalhes), "Erro", JOptionPane.ERROR_MESSAGE));
	}

	@Override
	public RespostaConfirmacao exibirConfirmacao(String mensagem) {
		// TODO não exibindo
		return SwingUtils.invokeAndWait(() -> JOptionPane.showConfirmDialog(null, mensagem, "Confirmação",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ? RespostaConfirmacao.SIM : RespostaConfirmacao.NAO);
	}
}
