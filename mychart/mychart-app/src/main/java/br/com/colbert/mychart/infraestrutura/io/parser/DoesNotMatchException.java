package br.com.colbert.mychart.infraestrutura.io.parser;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Exceção lançada quando um padrão não é identificado dentro de uma {@link String}.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 */
public class DoesNotMatchException extends ParserException {

	private static final long serialVersionUID = 6796154854905424013L;

	public DoesNotMatchException(Pattern pattern, String texto) {
		super(StringUtils.LF + "Padrão: " + pattern + StringUtils.LF + "Fonte: " + texto);
	}
}
