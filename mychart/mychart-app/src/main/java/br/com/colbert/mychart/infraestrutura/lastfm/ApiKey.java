package br.com.colbert.mychart.infraestrutura.lastfm;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando a API Key da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface ApiKey {

}
