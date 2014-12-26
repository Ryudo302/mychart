package br.com.colbert.mychart.infraestrutura.providers;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.FileSystemCache;

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
	public File cacheDir(@DiretorioBase File baseDir) {
		return new File(baseDir, "lastfm-cache");
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
	 * @return a instância criada
	 */
	@Produces
	public Caller caller(@WsBaseUrl String baseUrl, @TituloAplicacao(Formato.APENAS_NOME) String nomeApp,
			@CacheDirectory File cacheDir) {
		Caller caller = Caller.getInstance();
		caller.setApiRootUrl(baseUrl);
		caller.setUserAgent(nomeApp);
		caller.setCache(new FileSystemCache(cacheDir));
		return caller;
	}
}
