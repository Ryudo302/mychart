package br.com.colbert.mychart.infraestrutura.eventos.app;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando um evento de mudança no status da aplicação.
 * 
 * @author Thiago Colbert
 * @since 17/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER, TYPE })
@Retention(RUNTIME)
@Documented
public @interface StatusAplicacao {

	/**
	 * O tipo de status em que a aplicação se encontra.
	 * 
	 * @return o tipo de status
	 */
	TipoStatusAplicacao value();
}
