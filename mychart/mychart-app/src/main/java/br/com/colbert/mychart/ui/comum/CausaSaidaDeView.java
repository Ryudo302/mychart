package br.com.colbert.mychart.ui.comum;

/**
 * As causas pelas quais uma view pode ser fechada.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
public enum CausaSaidaDeView {

	/**
	 * Ocorreu um evento de confirmação / salvamento na view.
	 */
	CONFIRMACAO,

	/**
	 * Ocorreu um evento de fechar / cancelar na view.
	 */
	CANCELAMENTO,
}
