package br.com.colbert.mychart.dominio.topmusical.validator;

import java.util.Objects;

import javax.validation.*;

/**
 * Validador de números de posições.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
public class NumeroPosicaoValidator implements ConstraintValidator<NumeroPosicao, Integer> {

	// TODO Não funciona
	// @Inject
	// private TopMusicalConfiguration topMusicalConfig;
	private Integer quantidadePosicoes = Integer.MAX_VALUE;

	@Override
	public void initialize(NumeroPosicao constraintAnnotation) {
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return Objects.isNull(value) || (value > 0 && value <= quantidadePosicoes);
	}
}
