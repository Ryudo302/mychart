package br.com.colbert.mychart.infraestrutura.eventos.crud;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando um evento CRUD.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER, TYPE })
@Retention(RUNTIME)
public @interface OperacaoCrud {

	/**
	 * O tipo de operação.
	 * 
	 * @return o tipo de operação
	 */
	TipoOperacaoCrud value();
}
