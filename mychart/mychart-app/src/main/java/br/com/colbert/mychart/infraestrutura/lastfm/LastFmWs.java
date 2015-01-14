package br.com.colbert.mychart.infraestrutura.lastfm;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
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
public class LastFmWs implements ArtistaWs, CancaoWs, Serializable {

	private static final long serialVersionUID = -718202376554630609L;

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
			if (responseCode != 400) {
				logger.warn("PING error: a conexão com a URL {} retornou o código HTTP: {}", baseUrl, responseCode);
				return false;
			}

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
			resultadosConsulta = Artist.search(exemplo.getNome().toLowerCase(), apiKey);
			logger.debug("Resultado da consulta: {}", resultadosConsulta);
		} catch (CallException exception) {
			throw tratarExcecao(exception, exemplo);
		}

		Result result = caller.getLastResult();
		logger.debug("Resultado da operação: {}", result.getStatus());

		if (result.isSuccessful()) {
			List<Artista> artistas = new ArrayList<>(resultadosConsulta.size() / 2);
			resultadosConsulta.stream().filter(artist -> StringUtils.isNotBlank(artist.getMbid()))
					.forEach(artist -> artistas.add(criarArtista(artist)));
			return artistas;
		} else {
			throw new LastFmException(result);
		}
	}

	private Artista criarArtista(Artist artist) {
		return new Artista(artist.getMbid(), artist.getName(), TipoArtista.DESCONHECIDO);
	}

	@Override
	public Collection<Cancao> consultarPor(Cancao exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		Collection<Track> resultadosConsulta;
		try {
			resultadosConsulta = Track.search(exemplo.getNomeArtistaPrincipal(), exemplo.getTitulo(), Integer.MAX_VALUE, apiKey);
			logger.debug("Resultado da consulta: {}", resultadosConsulta);
		} catch (CallException exception) {
			throw tratarExcecao(exception, exemplo);
		}

		Result result = caller.getLastResult();
		logger.debug("Resultado da operação: {}", result.getStatus());

		if (result.isSuccessful()) {
			List<Cancao> cancoes = new ArrayList<>(resultadosConsulta.size() / 2);
			resultadosConsulta.stream().filter(track -> StringUtils.isNotBlank(track.getMbid())).forEach(track -> {
				try {
					cancoes.add(criarCancao(track));
				} catch (LastFmException exception) {
					throw new RuntimeException("Erro ao converter canção: " + track, exception);
				}
			});
			return cancoes;
		} else {
			throw new LastFmException(result);
		}
	}

	private Cancao criarCancao(Track track) throws LastFmException {
		return new Cancao(track.getMbid(), track.getName(), new Artista(getArtistId(track), track.getArtist(),
				TipoArtista.DESCONHECIDO));
	}

	private String getArtistId(Track track) throws LastFmException {
		logger.debug("Obtendo informações da canção através do seu MBID: {} - {}", track.getName(), track.getMbid());
		Track info = Track.getInfo(null, track.getMbid(), apiKey);

		Result result = caller.getLastResult();
		logger.debug("Resultado da operação: {}", result.getStatus());

		if (result.isSuccessful()) {
			return info.getArtistMbid();
		} else {
			logger.debug("Falhou. Obtendo informações da canção através do artista e do título: {} - {}", track.getName(),
					track.getArtist());
			info = Track.getInfo(track.getArtist(), track.getName(), apiKey);
		}

		if (!StringUtils.equals(track.getArtist(), info.getArtist())) {
			throw new LastFmException("Track.search e Track.getInfo retornaram artistas diferentes para a canção: "
					+ track.getName(), null);
		}

		return info.getArtistMbid();
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
