package br.com.colbert.mychart.infraestrutura.providers;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.validation.*;

/**
 * Classe que provê acesso a objetos relacionados à Java Bean Validation;
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
@ApplicationScoped
public final class ValidationProvider implements Serializable {

	private static final long serialVersionUID = -730796206381622321L;

	/**
	 * Obtém uma instância de {@link ValidatorFactory}.
	 * 
	 * @return a instância criada
	 */
	@Produces
	@ApplicationScoped
	public ValidatorFactory validatorFactory() {
		return Validation.buildDefaultValidatorFactory();
	}

	/**
	 * Obtém uma instância de {@link Validator}.
	 * 
	 * @param validatorFactory
	 *            fábrica utilizada na criação das instâncias
	 * @return a instância criada
	 */
	@Produces
	public Validator validator(ValidatorFactory validatorFactory) {
		return validatorFactory.getValidator();
	}
}
