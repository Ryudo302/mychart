package br.com.colbert.mychart.infraestrutura.providers;

import java.io.File;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.*;

import br.com.colbert.mychart.infraestrutura.info.*;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao.Formato;
import br.com.colbert.mychart.infraestrutura.lastfm.*;

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
	@WsBaseUrl
	@Singleton
	public String wsUrl() {
		return "http://ws.audioscrobbler.com/2.0/";
	}

	/**
	 * Obtém a <em>API Key</em> da LastFM.
	 * 
	 * @return a chave da API
	 */
	@Produces
	@ApiKey
	@Singleton
	public String apiKey() {
		return "6747f6d7194dfd2edcea226c96e395cb";
	}

	/**
	 * Obtém a <em>API Secret</em> da LastFM.
	 * 
	 * @return a senha da API
	 */
	@Produces
	@ApiSecret
	@Singleton
	public String apiSecret() {
		return "aa672af27375b77bc074d4f1b55d1f07";
	}

	/**
	 * Obtém referência para o diretório a ser utilizado para fazer cache dos resultados retornados pelos WebServices da LastFM.
	 * 
	 * @return o diretório de cache
	 */
	@Produces
	@CacheDirectory
	@Singleton
	public File cacheDir(@DiretorioBase File baseDir) {
		return new File(baseDir, "lastfm-cache");
	}

	/**
	 * Obtém uma política de expiração de cache.
	 * 
	 * @return a política de expiração
	 */
	@Produces
	@Singleton
	public ExpirationPolicy expirationPolicy() {
		return new DefaultExpirationPolicy() {

			@Override
			public long getExpirationTime(String method, Map<String, String> params) {
				if (StringUtils.equals(method, "artist.search") || StringUtils.equals(method, "track.search")) {
					return DefaultExpirationPolicy.ONE_WEEK;
				} else {
					return super.getExpirationTime(method, params);
				}
			}
		};
	}

	/**
	 * Obtém uma instância de {@link Caller} para as operações envolvendo os WebService da LastFM.
	 * 
	 * @param baseUrl
	 *            a URL base dos WebServices da LastFM
	 * @param nomeApp
	 *            o nome da aplicação
	 * @param cacheDir
	 *            diretório utilizado para cache
	 * @param expirationPolicy
	 *            política a ser utilizada quanto à expiração do cache
	 * @return a instância criada
	 */
	@Produces
	@Singleton
	public Caller caller(@WsBaseUrl String baseUrl, @TituloAplicacao(Formato.APENAS_NOME) String nomeApp,
			@CacheDirectory File cacheDir, ExpirationPolicy expirationPolicy) {
		Caller caller = Caller.getInstance();
		caller.setApiRootUrl(baseUrl);
		caller.setUserAgent(nomeApp);

		FileSystemCache cache = new FileSystemCache(cacheDir);
		cache.setExpirationPolicy(expirationPolicy);
		caller.setCache(cache);

		return caller;
	}
}
