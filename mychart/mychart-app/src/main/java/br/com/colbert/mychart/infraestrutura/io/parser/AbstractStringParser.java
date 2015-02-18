package br.com.colbert.mychart.infraestrutura.io.parser;

import java.io.Serializable;
import java.util.regex.*;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

/**
 * Implementação de {@link StringParser} que fornece métodos utilizados por todas as implementações.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 *
 * @param <T>
 *            o tipo de dado extraído
 */
public abstract class AbstractStringParser<T> implements StringParser<T>, Serializable {

	private static final long serialVersionUID = 2510405146831994676L;

	@Inject
	private transient Logger logger;

	@Override
	public final T parse(String fonte) {
		logger.debug("Processando: '{}'", fonte);
		Validate.notEmpty(fonte, "fonte");

		Pattern pattern = getPattern();
		logger.debug("Regex: {}", pattern);

		Matcher matcher = pattern.matcher(fonte);
		if (!matcher.find()) {
			throw new DoesNotMatchException(pattern, fonte);
		}

		String group = matcher.group(1);
		logger.debug("Grupo identificado: '{}'", group);
		return convert(group);
	}

	/**
	 * Obtém o padrão (<em>regex</em>) utilizado para identificar os dados a serem extraídos.
	 * 
	 * @return o padrão utilizado
	 */
	protected abstract Pattern getPattern();

	/**
	 * Converte a String identificada para o tipo de dado final.
	 * 
	 * @param group
	 *            String representando o dado extraído
	 * @return instância representando o dado extraído
	 */
	protected abstract T convert(String group);
}
