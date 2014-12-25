package br.com.colbert.mychart.infraestrutura.lastfm.json;

import java.io.*;
import java.util.*;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.gson.*;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Classe responsável pela conversão das respostas JSON dos serviços da LastFM em objetos {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
public class CancaoJsonConverter {

	@Inject
	private transient Logger logger;
	@Inject
	private transient Gson gson;

	/**
	 * Obtém uma coleção de {@link Cancao} a partir de um JSON.
	 * 
	 * @param jsonStream
	 *            <em>stream</em> de leitura do JSON
	 * @return a coleção de canções
	 * @throws IOException
	 *             caso ocorra algum erro de leitura do <em>stream</em>
	 * @throws JsonParseException
	 *             caso ocorra algum erro na conversão do JSON
	 */
	@SuppressWarnings("unchecked")
	public Collection<Cancao> fromJson(InputStream jsonStream) throws IOException {
		try (InputStreamReader reader = new InputStreamReader(jsonStream)) {
			Map<String, Object> lastFmResults = (Map<String, Object>) gson.fromJson(reader, Map.class);
			Map<String, Object> results = (Map<String, Object>) lastFmResults.get("results");

			if (Integer.valueOf((String) results.get("opensearch:totalResults")) > 0) {
				Map<String, Object> trackMatches = (Map<String, Object>) results.get("trackmatches");
				List<Map<String, Object>> tracks = (List<Map<String, Object>>) trackMatches.get("track");
				logger.debug("Convertendo: {}", tracks);

				Set<Cancao> cancoes = new HashSet<>(tracks.size());
				tracks.stream()
						.filter(track -> StringUtils.isNotBlank((CharSequence) track.get("mbid")))
						.forEach(
								track -> cancoes.add(new Cancao((String) track.get("name"), new Artista((String) track
										.get("artist"), TipoArtista.DESCONHECIDO))));

				logger.debug("Resultado: {}", cancoes);
				return cancoes;
			} else {
				return CollectionUtils.emptyCollection();
			}
		}
	}
}