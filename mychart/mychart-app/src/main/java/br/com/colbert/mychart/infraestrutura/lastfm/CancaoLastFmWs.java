package br.com.colbert.mychart.infraestrutura.lastfm;

import java.io.IOException;
import java.util.*;

import javax.inject.Inject;

import com.google.gson.JsonParseException;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.service.CancaoWs;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.mychart.infraestrutura.lastfm.api.*;
import br.com.colbert.mychart.infraestrutura.lastfm.json.CancaoJsonConverter;

/**
 * Implementação de {@link CancaoWs} utilizando os serviços da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public class CancaoLastFmWs implements CancaoWs {

	@Inject
	private transient LastFmWs lastFmWs;
	@Inject
	private transient CancaoJsonConverter jsonConverter;

	@Override
	public Collection<Cancao> consultarPor(Cancao exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		try {
			Map<Parametros, String> parametros = new HashMap<>(1);
			parametros.put(Parametros.CANCAO, exemplo.getTitulo());
			exemplo.getArtistaPrincipal().ifPresent(artista -> parametros.put(Parametros.ARTISTA, artista.getNome()));

			return jsonConverter.fromJson(lastFmWs.executarOperacao(Metodo.CONSULTA_CANCAO, parametros));
		} catch (LastFmException exception) {
			throw new ServiceException("Erro ao consultar canções a partir de exemplo: '" + exemplo
					+ "'. Erro ao acessar o serviço", exception);
		} catch (IOException | JsonParseException exception) {
			throw new ServiceException("Erro ao consultar canções a partir de exemplo: '" + exemplo
					+ "'. Erro ao processar a resposta do serviço", exception);
		}
	}
}
