package br.com.colbert.mychart.infraestrutura.info;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando uma coleção de e-mails dos desenvolvedores da aplicação.
 * 
 * @author Thiago Colbert
 * @since 01/02/2015
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface EmailsDesenvolvedores {

}
