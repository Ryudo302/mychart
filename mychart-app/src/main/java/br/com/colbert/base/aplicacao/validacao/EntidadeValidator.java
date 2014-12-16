package br.com.colbert.base.aplicacao.validacao;

import java.util.*;

import javax.inject.Inject;
import javax.validation.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.base.dominio.Entidade;

/**
 * Classe responsável por validar os dados de uma {@link Entidade} antes que ele seja enviado ao repositório.
 *
 * @author Thiago Colbert
 * @since 12/12/2014
 */
public class EntidadeValidator implements Validador<Entidade<?>> {

	@Inject
	private Logger logger;

	private Validator validator;

	@Inject
	public EntidadeValidator(Validator validator) {
		this.validator = validator;
	}

	@Override
	public void validar(Entidade<?> entidade) throws ValidacaoException {
		List<String> mensagens = new ArrayList<>();

		Set<ConstraintViolation<Entidade<?>>> violacoes = validator.validate(entidade);
		violacoes.forEach(violacao -> {
			logger.debug("Violação: {}", violacao);
			mensagens.add(new StringBuilder().append(getPathAsString(violacao)).append(StringUtils.SPACE).append(violacao.getMessage())
					.toString());
		});

		if (mensagens.size() > 0) {
			throw new ValidacaoException(mensagens);
		}
	}

	private String getPathAsString(ConstraintViolation<Entidade<?>> violacao) {
		StringBuilder pathNameBuilder = new StringBuilder();
		violacao.getPropertyPath().forEach(node -> pathNameBuilder.append(node.getName()).append('.'));
		return pathNameBuilder.deleteCharAt(pathNameBuilder.length() - 1).toString();
	}
}
