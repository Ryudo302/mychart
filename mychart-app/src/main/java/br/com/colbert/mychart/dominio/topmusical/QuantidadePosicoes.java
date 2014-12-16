package br.com.colbert.mychart.dominio.topmusical;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador que indica a quantidade de posições dos tops.
 * 
 * @author Thiago Colbert
 * @since 09/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER, TYPE })
@Retention(RUNTIME)
public @interface QuantidadePosicoes {

}
