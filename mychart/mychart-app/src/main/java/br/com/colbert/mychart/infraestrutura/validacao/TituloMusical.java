package br.com.colbert.mychart.infraestrutura.validacao;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.validation.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * <p>
 * Validação que verifica se o objeto anotado é um título musical válido:
 * <ul>
 * <li>Não é <code>null</code></li>
 * <li>Não é uma {@link String} vazia ("")</li>
 * <li>Possui , no máximo, 255 caracteres</li>
 * </ul>
 * </p>
 * 
 * @author Thiago Colbert
 * @since 20/12/2014
 */
@NotBlank
@Size(max = 255)
@ReportAsSingleViolation
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface TituloMusical {

	String message() default "{br.com.colbert.mychart.constraints.TituloMusical.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
