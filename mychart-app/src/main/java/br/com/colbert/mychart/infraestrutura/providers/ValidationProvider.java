package br.com.colbert.mychart.infraestrutura.providers;

import javax.enterprise.inject.Produces;
import javax.validation.*;

/**
 * Classe que provê acesso a objetos relacionados à Java Bean Validation;
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
public final class ValidationProvider {

	/**
	 * Obtém uma instância de {@link ValidatorFactory}.
	 * 
	 * @return a instância criada
	 */
	@Produces
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
