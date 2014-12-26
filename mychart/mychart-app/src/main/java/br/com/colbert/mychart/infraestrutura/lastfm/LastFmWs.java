package br.com.colbert.mychart.infraestrutura.lastfm;

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

		List<Artista> artistas = new ArrayList<>();
		Collection<Artist> resultasConsulta = Artist.search(exemplo.getNome(), apiKey);
		Result result = caller.getLastResult();
		logger.debug("Resultado da operação: {}", result.getStatus());

		if (result.isSuccessful()) {
			resultasConsulta.stream().filter(artist -> StringUtils.isNotBlank(artist.getMbid()))
					.forEach(artist -> artistas.add(new Artista(artist.getName(), TipoArtista.DESCONHECIDO)));
			return artistas;
		} else {
			throw new LastFmException(result);
		}
	}

	@Override
	public Collection<Cancao> consultarPor(Cancao exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		List<Cancao> cancoes = new ArrayList<>();
		Collection<Track> resultasConsulta = Track.search(exemplo.getNomeArtistaPrincipal(), exemplo.getTitulo(),
				Integer.MAX_VALUE, apiKey);
		Result result = caller.getLastResult();
		logger.debug("Resultado da operação: {}", result.getStatus());

		if (result.isSuccessful()) {
			resultasConsulta
					.stream()
					.filter(artist -> StringUtils.isNotBlank(artist.getMbid()))
					.forEach(
							track -> cancoes.add(new Cancao(track.getName(), new Artista(track.getArtist(),
									TipoArtista.DESCONHECIDO))));
			return cancoes;
		} else {
			throw new LastFmException(result);
		}
	}
}
