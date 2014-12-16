package br.com.colbert.mychart.dominio.topmusical.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.validation.*;

/**
 * Anotação utilizada para validar números de posições.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = NumeroPosicaoValidator.class)
@Documented
public @interface NumeroPosicao {

	String message() default "{br.com.colbert.mychart.constraints.numeroposicao}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
