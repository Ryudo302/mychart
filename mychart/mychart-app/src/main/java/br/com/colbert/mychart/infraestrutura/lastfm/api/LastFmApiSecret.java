package br.com.colbert.mychart.infraestrutura.lastfm.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando a API Secret da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface LastFmApiSecret {

}
