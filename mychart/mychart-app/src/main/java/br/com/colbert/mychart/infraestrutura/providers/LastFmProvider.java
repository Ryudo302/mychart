package br.com.colbert.mychart.infraestrutura.providers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import br.com.colbert.mychart.infraestrutura.lastfm.api.*;

/**
 * Provê instâncias utilizadas pelos serviços da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 *
 */
@ApplicationScoped
public class LastFmProvider {

	/**
	 * Obtém a URL base dos WebServices da LastFM.
	 * 
	 * @return a URL base
	 */
	@Produces
	@LastFmWsUrl
	public String wsUrl() {
		return "http://ws.audioscrobbler.com/2.0/";
	}

	/**
	 * Obtém a <em>API Key</em> da LastFM.
	 * 
	 * @return a chave da API
	 */
	@Produces
	@LastFmApiKey
	public String apiKey() {
		return "6747f6d7194dfd2edcea226c96e395cb";
	}

	/**
	 * Obtém a <em>API Secret</em> da LastFM.
	 * 
	 * @return a senha da API
	 */
	@Produces
	@LastFmApiSecret
	public String apiSecret() {
		return "aa672af27375b77bc074d4f1b55d1f07";
	}
}
