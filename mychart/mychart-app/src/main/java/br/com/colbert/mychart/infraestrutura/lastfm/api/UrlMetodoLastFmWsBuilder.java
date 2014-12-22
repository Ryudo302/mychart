package br.com.colbert.mychart.infraestrutura.lastfm.api;

import java.net.*;
import java.util.*;

import org.apache.commons.lang3.builder.Builder;

/**
 * {@link Builder} que auxilia na construção de uma {@link URL} para execução de um método dos WebServices da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public class UrlMetodoLastFmWsBuilder implements Builder<URL> {

	private static final String FORMATO_JSON = "json";

	private final String baseUrl;
	private final Metodo metodo;
	private final Map<Parametros, String> parametros;

	public UrlMetodoLastFmWsBuilder(String baseUrl, String apiKey, Metodo metodo) {
		this.baseUrl = baseUrl;
		this.metodo = metodo;
		this.parametros = new HashMap<>();
		append(Parametros.API_KEY, apiKey).append(Parametros.FORMATO, FORMATO_JSON);
	}

	/**
	 * Acrescenta um parâmetro à URL.
	 * 
	 * @param nome
	 *            do parâmetro
	 * @param valor
	 *            do parâmetro
	 * @throws NullPointerException
	 *             caso o nome ou o valor do parâmetro sejam <code>null</code>
	 * @return <code>this</code> (para chamadas encadeadas)
	 * @see #appendAll(Map)
	 */
	public final UrlMetodoLastFmWsBuilder append(Parametros nome, String valor) {
		parametros.put(Objects.requireNonNull(nome, "O nome do parâmetro é obrigatório"),
				Objects.requireNonNull(valor, "O valor do parâmetro é obrigatório"));
		return this;
	}

	/**
	 * Acrescenta os parâmetros informados à URL.
	 * 
	 * @param parametros
	 *            a serem adicionados
	 * @return <code>this</code> (para chamadas encadeadas)
	 */
	public UrlMetodoLastFmWsBuilder appendAll(Map<Parametros, String> parametros) {
		parametros.forEach((nome, valor) -> append(nome, valor));
		return this;
	}

	@Override
	public URL build() {
		try {
			StringBuilder urlBuilder = new StringBuilder().append(baseUrl).append('?').append(Parametros.METODO).append('=')
					.append(metodo.getNome());
			parametros.forEach((chave, valor) -> urlBuilder.append('&').append(chave).append('=').append(valor));
			return new URL(urlBuilder.toString());
		} catch (MalformedURLException exception) {
			throw new IllegalStateException("Erro ao gerar a URL", exception);
		}
	}
}
