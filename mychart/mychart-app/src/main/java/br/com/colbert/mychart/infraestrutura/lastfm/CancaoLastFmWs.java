package br.com.colbert.mychart.infraestrutura.lastfm;

import java.io.*;
import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.gson.*;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.service.CancaoWs;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.mychart.infraestrutura.lastfm.api.*;

/**
 * Implementação de {@link CancaoWs} utilizando os serviços da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public class CancaoLastFmWs implements CancaoWs {

	@Inject
	private transient Logger logger;
	@Inject
	private transient Gson gson;

	@Inject
	private transient LastFmWs lastFmWs;

	@Override
	public Set<Cancao> consultarPor(Cancao exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		try {
			Map<Parametros, String> parametros = new HashMap<>(1);
			parametros.put(Parametros.CANCAO, exemplo.getTitulo());
			exemplo.getArtistaPrincipal().ifPresent(artista -> parametros.put(Parametros.ARTISTA, artista.getNome()));

			return readCancoes(lastFmWs.executarOperacao(Metodo.CONSULTA_CANCAO, parametros));
		} catch (LastFmException exception) {
			throw new ServiceException("Erro ao consultar canções a partir de exemplo: '" + exemplo
					+ "'. Erro ao acessar o serviço", exception);
		} catch (IOException | JsonParseException exception) {
			throw new ServiceException("Erro ao consultar canções a partir de exemplo: '" + exemplo
					+ "'. Erro ao processar a resposta do serviço", exception);
		}
	}

	@SuppressWarnings("unchecked")
	private Set<Cancao> readCancoes(InputStream inputStream) throws IOException {
		try (InputStreamReader reader = new InputStreamReader(inputStream)) {
			Map<String, Object> lastFmResults = (Map<String, Object>) gson.fromJson(reader, Map.class);
			Map<String, Object> results = (Map<String, Object>) lastFmResults.get("results");

			if (Integer.valueOf((String) results.get("opensearch:totalResults")) > 0) {
				Map<String, Object> trackMatches = (Map<String, Object>) results.get("trackmatches");
				List<Map<String, Object>> tracks = (List<Map<String, Object>>) trackMatches.get("track");
				logger.debug("Canções retornadas pela LastFM: {}", tracks);

				Set<Cancao> cancoes = new HashSet<>(tracks.size());
				tracks.stream().filter(track -> {
					return StringUtils.isNotBlank((CharSequence) track.get("mbid"));
				}).forEach(track -> {
					cancoes.add(new Cancao((String) track.get("title"), null));
				});
				return cancoes;
			} else {
				return Collections.emptySet();
			}
		}
	}
}
