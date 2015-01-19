package br.com.colbert.mychart.infraestrutura.swing.worker;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando uma tela que é exibida quando é aguardada a execução de uma tarefa em <em>background</em>.
 * 
 * @author Thiago Colbert
 * @since 19/01/2015
 */
@Qualifier
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
@Documented
public @interface LoadingView {

}
