package br.com.colbert.mychart.infraestrutura.exception;

import java.text.MessageFormat;

/**
 * Exceção lançada quando ocorre tentativa de executar alguma operação sobre um elemento que ainda não existe dentro de uma
 * coleção.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ElementoNaoExistenteException extends Exception {

	private static final long serialVersionUID = -7998141000794889591L;

	private static final String FORMATO_MENSAGEM = "{0} ainda não existe: {1}";

	/**
	 * Cria uma nova exceção informando qual o elemento que ainda não existe, além de um nome para ser adicionado à mensagem da
	 * exceção.
	 * 
	 * @param nome
	 *            o nome <em>user-friendly</em> do elemento
	 * @param elemento
	 *            o elemento
	 */
	public <T> ElementoNaoExistenteException(String nome, T elemento) {
		super(MessageFormat.format(FORMATO_MENSAGEM, nome, elemento));
	}

	/**
	 * Cria uma nova exceção informando qual o elemento que ainda não existe. Como nome do elemento na mensagem gerada, será
	 * utilizado o nome de sua classe (simples, sem pacote).
	 * 
	 * @param elemento
	 *            o elemento
	 */
	public <T> ElementoNaoExistenteException(T elemento) {
		this(elemento.getClass().getSimpleName(), elemento);
	}
}
