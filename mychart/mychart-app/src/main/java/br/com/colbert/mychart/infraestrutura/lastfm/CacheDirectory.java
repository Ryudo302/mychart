package br.com.colbert.mychart.infraestrutura.lastfm;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando o diretório utilizado para fazer cache dos resultados das consultas feitas à LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface CacheDirectory {

}
