package br.com.colbert.mychart.ui.comum.messages;

import br.com.colbert.base.ui.View;

/**
 * {@link View} que exibe mensagens geradas pela aplicação.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public interface MessagesView extends View {

	void adicionarMensagemSucesso(String sumario);

	void adicionarMensagemAlerta(String mensagem);

	/**
	 * Adiciona uma mensagem de erro à tela.
	 * 
	 * @param resumo
	 *            da mensagem de erro
	 * @param detalhes
	 *            da mensagem de erro
	 */
	void adicionarMensagemErro(String resumo, String detalhes);

	/**
	 * Exibe uma mensagem de confirmação na tela.
	 * 
	 * @param mensagem
	 *            conteúdo da mensagem
	 * @return a resposta do usuário
	 */
	RespostaConfirmacao exibirConfirmacao(String mensagem);
}
