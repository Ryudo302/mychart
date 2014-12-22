package br.com.colbert.mychart.infraestrutura.lastfm.api;

import java.io.*;
import java.net.*;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * WebService da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
@ApplicationScoped
public class LastFmWs {

	private static final int HTTP_STATUS_OK = 200;

	@Inject
	private transient Logger logger;

	@Inject
	@LastFmWsUrl
	private transient String baseUrl;

	@Inject
	@LastFmApiKey
	private transient String apiKey;

	/**
	 * Executa uma operação com os parâmetros informados.
	 * 
	 * @param metodo
	 *            a ser executado
	 * @param parametros
	 *            a serem repassados para a chamada ao método
	 * @return <em>stream</em> de leitura do retorno do método
	 * @throws LastFmException
	 *             caso ocorra algum erro durante a comunicação com os WebServices da LastFM
	 */
	public InputStream executarOperacao(Metodo metodo, Map<Parametros, String> parametros) throws LastFmException {
		try {
			URL url = new UrlMetodoLastFmWsBuilder(baseUrl, apiKey, metodo).appendAll(parametros).build();
			logger.debug("Abrindo conexão com a URL: {}", url);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int responseCode = connection.getResponseCode();
			if (responseCode == HTTP_STATUS_OK) {
				return connection.getInputStream();
			} else {
				throw new LastFmException(metodo, parametros, "O serviço retornou como resposta o código HTTP " + responseCode);
			}
		} catch (IOException exception) {
			throw new LastFmException(metodo, parametros, exception);
		}
	}
}
