package br.com.colbert.mychart.infraestrutura.io.parser;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

/**
 * Identifica o número da posição a partir de uma {@link String} representando uma posição.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 */
public class NumeroPosicaoParser extends AbstractStringParser<Integer> {

	private static final long serialVersionUID = 5225409412268591493L;

	private static final String NUMERO_POSICAO_REGEX = "(\\d{1,2}) .*";

	private Pattern pattern;

	@PostConstruct
	protected void init() {
		pattern = Pattern.compile(NUMERO_POSICAO_REGEX);
	}

	@Override
	protected Pattern getPattern() {
		return pattern;
	}

	@Override
	protected Integer convert(String group) {
		return Integer.valueOf(group);
	}
}
