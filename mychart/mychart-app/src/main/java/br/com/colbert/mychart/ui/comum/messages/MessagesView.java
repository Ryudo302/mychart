package br.com.colbert.mychart.ui.comum.messages;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.JOptionPane;

import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;

/**
 * Classe que permite a exibição de mensagens na tela.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@ApplicationScoped
public class MessagesView implements Serializable {

	private static final long serialVersionUID = -4294537871800655560L;

	public void adicionarMensagemSucesso(String mensagem) {
		SwingUtils.invokeLater(() -> JOptionPane.showMessageDialog(null, mensagem, "Info", JOptionPane.INFORMATION_MESSAGE));
	}

	public void adicionarMensagemAlerta(String mensagem) {
		SwingUtils.invokeLater(() -> JOptionPane.showMessageDialog(null, mensagem, "Alerta", JOptionPane.WARNING_MESSAGE));
	}

	public void adicionarMensagemErro(String resumo, Throwable exception) {
		SwingUtils.invokeLater(() -> JOptionPane.showMessageDialog(null,
				MessageFormat.format("{0}:\n\n{1}\n\n", resumo, exception.getLocalizedMessage()), "Erro",
				JOptionPane.ERROR_MESSAGE));
	}

	public RespostaConfirmacao exibirConfirmacao(String mensagem) {
		return JOptionPane.showConfirmDialog(null, mensagem, "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ? RespostaConfirmacao.SIM
				: RespostaConfirmacao.NAO;
	}
}
