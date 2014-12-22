package br.com.colbert.mychart.infraestrutura.lastfm;

import java.io.*;
import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.gson.*;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.service.ArtistaWs;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.mychart.infraestrutura.lastfm.api.*;

/**
 * Implementação de {@link ArtistaWs} utilizando os serviços da LastFM.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaLastFmWs implements ArtistaWs {

	@Inject
	private transient Logger logger;
	@Inject
	private transient Gson gson;

	@Inject
	private transient LastFmWs lastFmWs;

	@Override
	public Set<Artista> consultarPor(Artista exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");
		try {
			Map<Parametros, String> parametros = new HashMap<>(1);
			parametros.put(Parametros.ARTISTA, exemplo.getNome());

			return readArtistas(lastFmWs.executarOperacao(Metodo.CONSULTA_ARTISTA, parametros));
		} catch (LastFmException exception) {
			throw new ServiceException("Erro ao consultar artistas a partir de exemplo: " + exemplo
					+ ". Erro ao acessar o serviço", exception);
		} catch (IOException | JsonParseException exception) {
			throw new ServiceException("Erro ao consultar artistas a partir de exemplo: '" + exemplo
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
