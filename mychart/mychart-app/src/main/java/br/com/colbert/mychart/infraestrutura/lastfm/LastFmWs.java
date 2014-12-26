package br.com.colbert.mychart.infraestrutura.lastfm;

import java.net.UnknownHostException;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
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

	@Override
	public Collection<Artista> consultarPor(Artista exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

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
			resultadosConsulta.stream().filter(artist -> StringUtils.isNotBlank(artist.getMbid()))
					.forEach(artist -> artistas.add(new Artista(artist.getName(), TipoArtista.DESCONHECIDO)));
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
							track -> cancoes.add(new Cancao(track.getName(), new Artista(track.getArtist(),
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
