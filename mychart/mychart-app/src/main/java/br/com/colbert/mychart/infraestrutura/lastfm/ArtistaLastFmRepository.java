package br.com.colbert.mychart.infraestrutura.lastfm;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.gson.*;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepositoryRemoto;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Implementação de {@link ArtistaRepositoryRemoto} que utiliza como fonte de dados os serviços da LastFM.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaLastFmRepository implements ArtistaRepositoryRemoto {

	private static final int HTTP_STATUS_OK = 200;

	private static final String API_KEY = "6747f6d7194dfd2edcea226c96e395cb";
	// secret aa672af27375b77bc074d4f1b55d1f07

	@Inject
	private transient Logger logger;
	@Inject
	private transient Gson gson;

	@Override
	public Set<Artista> consultarPor(String nome) throws RepositoryException {
		try {
			URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=" + nome + "&api_key=" + API_KEY
					+ "&format=json");
			logger.debug("Abrindo conexão com a URL: {}", url);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int responseCode = connection.getResponseCode();
			if (responseCode == HTTP_STATUS_OK) {
				return readArtistas(connection.getInputStream());
			} else {
				throw new RepositoryException("Erro ao consultar artistas da LastFM pelo nome: '" + nome
						+ "'. O serviço retornou como resposta o código HTTP " + responseCode);
			}
		} catch (IOException exception) {
			throw new RepositoryException("Erro ao consultar artistas da LastFM pelo nome: '" + nome
					+ "'. Erro ao acessar o serviço", exception);
		} catch (JsonParseException exception) {
			throw new RepositoryException("Erro ao consultar artistas da LastFM pelo nome: '" + nome
					+ "'. Erro ao processar a resposta do serviço", exception);
		}
	}

	@SuppressWarnings("unchecked")
	private Set<Artista> readArtistas(InputStream inputStream) throws IOException {
		try (InputStreamReader reader = new InputStreamReader(inputStream)) {
			Map<String, Object> lastFmResults = (Map<String, Object>) gson.fromJson(reader, Map.class);
			Map<String, Object> results = (Map<String, Object>) lastFmResults.get("results");

			if (Integer.valueOf((String) results.get("opensearch:totalResults")) > 0) {
				Map<String, Object> artistMatches = (Map<String, Object>) results.get("artistmatches");
				List<Map<String, Object>> artists = (List<Map<String, Object>>) artistMatches.get("artist");
				logger.debug("Artistas retornados pela LastFM: {}", artists);

				Set<Artista> artistas = new HashSet<>();
				artists.stream().filter(artist -> {
					return StringUtils.isNotBlank((CharSequence) artist.get("mbid"));
				}).forEach(artist -> {
					artistas.add(new Artista((String) artist.get("name"), TipoArtista.DESCONHECIDO));
				});
				return artistas;
			} else {
				return Collections.emptySet();
			}
		}
	}
}
