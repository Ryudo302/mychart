package br.com.colbert.mychart.ui.principal;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando que um objeto representa uma visualização (painel) que deve ser exibida dentro da tela principal.
 * 
 * @author Thiago Colbert
 * @since 02/02/2015
 */
@Qualifier
@Target({ TYPE, FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface PainelTelaPrincipal {

}
