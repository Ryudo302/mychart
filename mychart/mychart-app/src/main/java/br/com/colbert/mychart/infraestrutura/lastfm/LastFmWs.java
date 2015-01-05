package br.com.colbert.mychart.infraestrutura.lastfm;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

import de.umass.lastfm.*;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.service.ArtistaWs;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.service.CancaoWs;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;

/**
 * WebService da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
@ApplicationScoped
public class LastFmWs implements ArtistaWs, CancaoWs {

	@Inject
	private transient Logger logger;

	@Inject
	private transient Caller caller;

	@Inject
	@ApiKey
	private transient String apiKey;

	@Inject
	@WsBaseUrl
	private transient String baseUrl;

	/**
	 * Verifica se o serviço está acessível e respondendo.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean ping() {
		try {
			URLConnection connection = new URL(baseUrl).openConnection();
			int responseCode = ((HttpURLConnection) connection).getResponseCode();
			if (responseCode != 200) {
				logger.warn("PING error: a conexão com a URL {} retornou o código HTTP: {}", baseUrl, responseCode);
				return false;
			}

			InputStream inputStream = connection.getInputStream();
			int read = inputStream.read();
			if (read == -1) {
				logger.warn("PING error: leitura do stream da URL {} retornou o valor: {}", baseUrl, read);
				return false;
			}
			IOUtils.closeQuietly(inputStream);

			logger.info("PING successful!");
			return true;
		} catch (IOException exception) {
			logger.warn("PING error: " + ExceptionUtils.getRootCauseMessage(exception), exception);
			return false;
		}
	}

	@Override
	public Collection<Artista> consultarPor(Artista exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");
		if (StringUtils.isBlank(exemplo.getNome())) {
			logger.debug("Nenhum nome de artista informado - a consulta não será feita");
			return CollectionUtils.emptyCollection();
		}

		Collection<Artist> resultadosConsulta;
		try {
			resultadosConsulta = Artist.search(exemplo.getNome(), apiKey);
		} catch (CallException exception) {
			throw tratarExcecao(exception, exemplo);
		}

		Result result = caller.getLastResult();
		logger.debug("Resultado da operação: {}", result.getStatus());

		if (result.isSuccessful()) {
			List<Artista> artistas = new ArrayList<>(resultadosConsulta.size() / 2);
			logger.debug("Artistas encontrados: {}", resultadosConsulta);
			resultadosConsulta.stream().filter(artist -> StringUtils.isNotBlank(artist.getMbid()))
					.forEach(artist -> artistas.add(new Artista(artist.getMbid(), artist.getName(), TipoArtista.DESCONHECIDO)));
			return artistas;
		} else {
			throw new LastFmException(result);
		}
	}

	@Override
	public Collection<Cancao> consultarPor(Cancao exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		Collection<Track> resultadosConsulta;
		try {
			resultadosConsulta = Track.search(exemplo.getNomeArtistaPrincipal(), exemplo.getTitulo(), Integer.MAX_VALUE, apiKey);
		} catch (CallException exception) {
			throw tratarExcecao(exception, exemplo);
		}

		Result result = caller.getLastResult();
		logger.debug("Resultado da operação: {}", result.getStatus());

		if (result.isSuccessful()) {
			List<Cancao> cancoes = new ArrayList<>(resultadosConsulta.size() / 2);
			resultadosConsulta
					.stream()
					.filter(track -> StringUtils.isNotBlank(track.getMbid()))
					.forEach(
							track -> cancoes.add(new Cancao(track.getName(), new Artista(null, track.getArtist(),
									TipoArtista.DESCONHECIDO))));
			return cancoes;
		} else {
			throw new LastFmException(result);
		}
	}

	private <T> ServiceException tratarExcecao(CallException exception, T exemplo) throws ServicoInacessivelException,
			LastFmException {
		if (exception.getCause() instanceof UnknownHostException) {
			return new ServicoInacessivelException(exception);
		} else {
			return new LastFmException("Erro ao consultar a partir de exemplo: " + exemplo, exception);
		}
	}
}
