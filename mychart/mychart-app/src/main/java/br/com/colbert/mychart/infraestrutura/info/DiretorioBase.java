package br.com.colbert.mychart.infraestrutura.info;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando o diretório-base utilizado pela aplicação no sistema de arquivos do usuário.
 * 
 * @author Thiago Colbert
 * @since 18/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface DiretorioBase {

}
