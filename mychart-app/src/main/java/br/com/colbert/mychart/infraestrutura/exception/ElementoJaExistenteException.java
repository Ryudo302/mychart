package br.com.colbert.mychart.infraestrutura.exception;

import java.text.MessageFormat;

/**
 * Exceção indicando que houve tentativa de adicionar um elemento já existente em uma coleção.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ElementoJaExistenteException extends Exception {

	private static final long serialVersionUID = 7905925000194133456L;

	private static final String FORMATO_MENSAGEM = "{0} já existente: {1}";

	/**
	 * Cria uma nova exceção informando qual o elemento que já existe, além de um nome para ser adicionado à mensagem da exceção.
	 * 
	 * @param nome
	 *            o nome <em>user-friendly</em> do elemento
	 * @param elemento
	 *            o elemento
	 */
	public <T> ElementoJaExistenteException(String nome, T elemento) {
		super(MessageFormat.format(FORMATO_MENSAGEM, nome, elemento));
	}

	/**
	 * Cria uma nova exceção informando qual o elemento que já existe. Como nome do elemento na mensagem gerada, será utilizado o
	 * nome de sua classe (simples, sem pacote).
	 * 
	 * @param elemento
	 *            o elemento
	 */
	public <T> ElementoJaExistenteException(T elemento) {
		this(elemento.getClass().getSimpleName(), elemento);
	}
}
