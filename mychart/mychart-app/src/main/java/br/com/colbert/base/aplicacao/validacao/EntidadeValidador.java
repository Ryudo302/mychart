package br.com.colbert.base.aplicacao.validacao;

import java.io.Serializable;
import java.util.*;

import javax.inject.Inject;
import javax.validation.*;

import org.slf4j.Logger;

import br.com.colbert.base.dominio.Entidade;

/**
 * Classe responsável por validar os dados de uma {@link Entidade} antes que ele seja enviado ao repositório.
 *
 * @author Thiago Colbert
 * @since 12/12/2014
 */
public abstract class EntidadeValidador<T extends Entidade<?>> implements Serializable, Validador<T> {

	private static final long serialVersionUID = 8112978327432267717L;

	private final Validator validator;

	@Inject
	private Logger logger;

	public EntidadeValidador(Validator validator) {
		this.validator = validator;
	}

	@Override
	public void validar(T entidade) throws ValidacaoException {
		List<String> mensagens = new ArrayList<>();

		Set<ConstraintViolation<Entidade<?>>> violacoes = validator.validate(entidade);
		violacoes.forEach(violacao -> {
			logger.debug("Violação: {}", violacao);
			mensagens.add(new StringBuilder().append(violacao.getMessage()).toString());
		});

		if (mensagens.size() > 0) {
			throw new ValidacaoException(mensagens);
		}
	}
}
