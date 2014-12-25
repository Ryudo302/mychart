package br.com.colbert.mychart.infraestrutura.lastfm;

import java.io.IOException;
import java.util.*;

import javax.inject.Inject;

import com.google.gson.JsonParseException;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.service.ArtistaWs;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.mychart.infraestrutura.lastfm.api.*;
import br.com.colbert.mychart.infraestrutura.lastfm.json.ArtistaJsonConverter;

/**
 * Implementação de {@link ArtistaWs} utilizando os serviços da LastFM.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaLastFmWs implements ArtistaWs {

	@Inject
	private transient LastFmWs lastFmWs;
	@Inject
	private transient ArtistaJsonConverter jsonConverter;

	@Override
	public Collection<Artista> consultarPor(Artista exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");
		try {
			Map<Parametros, String> parametros = new HashMap<>(1);
			parametros.put(Parametros.ARTISTA, exemplo.getNome());

			return jsonConverter.fromJson(lastFmWs.executarOperacao(Metodo.CONSULTA_ARTISTA, parametros));
		} catch (LastFmException exception) {
			throw new ServiceException("Erro ao consultar artistas a partir de exemplo: " + exemplo
					+ ". Erro ao acessar o serviço", exception);
		} catch (IOException | JsonParseException exception) {
			throw new ServiceException("Erro ao consultar artistas a partir de exemplo: '" + exemplo
					+ "'. Erro ao processar a resposta do serviço", exception);
		}
	}
}
