package br.com.colbert.mychart.dominio.artista.service;

import java.util.Collection;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;

/**
 * Serviço com operações na web envolvendo {@link Artista}s.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public interface ArtistaWs {

	/**
	 * Faz uma consulta por artistas utilizando como parâmetro um artista de exemplo.
	 * 
	 * @param exemplo
	 *            a ser utilizado na consulta
	 * @return os artistas encontrados (pode ser vazio)
	 * @throws ServiceException
	 *             caso ocorra algum erro na consulta
	 */
	Collection<Artista> consultarPor(Artista exemplo) throws ServiceException;

}