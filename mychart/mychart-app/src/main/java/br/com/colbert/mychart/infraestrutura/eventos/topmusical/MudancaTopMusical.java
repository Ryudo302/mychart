package br.com.colbert.mychart.infraestrutura.eventos.topmusical;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando um evento de mudança dentro de um top musical.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER, TYPE })
@Retention(RUNTIME)
public @interface MudancaTopMusical {

	/**
	 * O tipo de mudança no top musical.
	 * 
	 * @return o tipo de mudança
	 */
	TipoMudancaTopMusical value();
}
