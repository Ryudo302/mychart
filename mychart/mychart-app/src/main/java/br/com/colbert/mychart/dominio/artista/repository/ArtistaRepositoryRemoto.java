package br.com.colbert.mychart.dominio.artista.repository;

import java.util.Collection;

import br.com.colbert.base.dominio.Repository;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Repositório remoto de {@link Artista}s. Ele difere de um repositório local por ser <em>read-only</em>.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 * @see CancaoRepositoryLocal
 */
public interface ArtistaRepositoryRemoto extends Repository<Artista, Integer> {

	/**
	 * Faz uma consulta por artistas a partir de seu nome.
	 * 
	 * @param nome
	 *            a ser utilizado na consulta
	 * @return os artistas encontrados (pode ser uma lista vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro durante a operação
	 */
	Collection<? extends Artista> consultarPor(String nome) throws RepositoryException;
}
