package br.com.colbert.base.aplicacao.validacao;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Exceção lançada quando ocorre um erro de validação no domínio.
 *
 * @author Thiago Colbert
 * @since 12/12/2014
 */
public class ValidacaoException extends Exception {

	private static final long serialVersionUID = 2847072027443175544L;

	private final List<String> errosValidacao;

	/**
	 * Cria uma nova exceção com as mensagens de erro de validação informadas.
	 *
	 * @param errosValidacao
	 *            as mensagens de erros de validação
	 * @throws NullPointerException
	 *             caso o parâmetro seja <code>null</code>
	 */
	public ValidacaoException(List<String> errosValidacao) {
		super(toString(errosValidacao));
		this.errosValidacao = new ArrayList<>(Objects.requireNonNull(errosValidacao, "As mensagens devem ser informadas"));
	}

	/**
	 * Cria uma nova exceção com as mensagens de erro de validação informadas.
	 *
	 * @param errosValidacao
	 *            as mensagens de erros de validação
	 * @throws NullPointerException
	 *             caso o parâmetro seja <code>null</code>
	 */
	public ValidacaoException(String... errosValidacao) {
		this(Arrays.asList(errosValidacao));
	}

	private static String toString(List<String> mensagens) {
		StringBuilder builder = new StringBuilder();
		mensagens.forEach(mensagem -> builder.append(mensagem).append(StringUtils.LF));
		return builder.toString();
	}

	/**
	 * Obtém as mensagens de erros de validação.
	 *
	 * @return as mensagens
	 */
	public List<String> getErrosValidacao() {
		return Collections.unmodifiableList(errosValidacao);
	}
}
