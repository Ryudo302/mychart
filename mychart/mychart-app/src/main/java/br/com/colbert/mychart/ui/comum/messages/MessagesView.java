package br.com.colbert.mychart.ui.comum.messages;

/**
 * EntidadeView que exibe mensagens geradas pela aplicação.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public interface MessagesView {

	void adicionarMensagemSucesso(String mensagem);
	
	void adicionarMensagemAlerta(String mensagem);
	
	void adicionarMensagemErro(String mensagem);
}
