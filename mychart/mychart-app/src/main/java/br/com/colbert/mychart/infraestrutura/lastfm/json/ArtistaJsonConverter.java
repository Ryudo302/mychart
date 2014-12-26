package br.com.colbert.mychart.infraestrutura.lastfm.json;

import static br.com.colbert.mychart.infraestrutura.lastfm.json.JsonConverterUtils.getOrThrowExceptionIfNotExists;

import java.io.*;
import java.util.*;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.gson.*;

import br.com.colbert.mychart.dominio.artista.*;

/**
 * Classe responsável pela conversão das respostas JSON dos serviços da LastFM em objetos {@link Artista}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
public class ArtistaJsonConverter {

	@Inject
	private transient Logger logger;
	@Inject
	private transient Gson gson;

	/**
	 * Obtém uma coleção de {@link Artista}s a partir de um JSON.
	 * 
	 * @param jsonStream
	 *            <em>stream</em> de leitura do JSON
	 * @return a coleção de artistas
	 * @throws IOException
	 *             caso ocorra algum erro de leitura do <em>stream</em>
	 * @throws JsonParseException
	 *             caso ocorra algum erro na conversão do JSON
	 */
	@SuppressWarnings("unchecked")
	public Collection<Artista> fromJson(InputStream jsonStream) throws IOException {
		try (InputStreamReader reader = new InputStreamReader(jsonStream)) {
			Map<String, Object> lastFmResults = (Map<String, Object>) gson.fromJson(reader, Map.class);
			Map<String, Object> results = getOrThrowExceptionIfNotExists(lastFmResults, "results");

			if (Integer.valueOf(getOrThrowExceptionIfNotExists(results, "opensearch:totalResults")) > 0) {
				Map<String, Object> artistMatches = getOrThrowExceptionIfNotExists(results, "artistmatches");
				List<Map<String, Object>> artists = getOrThrowExceptionIfNotExists(artistMatches, "artist");
				logger.debug("Convertendo: {}", artists);

				Set<Artista> artistas = new HashSet<>();
				artists.stream().filter(artist -> {
					return StringUtils.isNotBlank(getOrThrowExceptionIfNotExists(artist, "mbid"));
				}).forEach(artist -> {
					artistas.add(new Artista(getOrThrowExceptionIfNotExists(artist, "name"), TipoArtista.DESCONHECIDO));
				});

				logger.debug("Resultado: {}", artistas);
				return artistas;
			} else {
				return CollectionUtils.emptyCollection();
			}
		}
	}
}