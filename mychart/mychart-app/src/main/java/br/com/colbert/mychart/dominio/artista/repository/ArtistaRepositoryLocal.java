package br.com.colbert.mychart.dominio.artista.repository;

import java.util.Collection;

import br.com.colbert.base.dominio.CrudRepository;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Repositório local de {@link Artista}s. Ele difere de um repositório remoto por permitir operações de inserção, remoção e/ou
 * atualização de elementos.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 * @see ArtistaRepositoryRemoto
 */
public interface ArtistaRepositoryLocal extends CrudRepository<Artista, Integer> {

	/**
	 * Consulta por artistas que tenham o nome exatamente igual ao informado. A consulta não é <em>case-sensitive</em>.
	 * 
	 * @param nome
	 *            a ser utilizado na consulta
	 * @return os artistas que possuem o nome informado (pode estar vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro não-tratado durante a operação
	 */
	public Collection<Artista> consultarPorNomeExato(String nome) throws RepositoryException;
}
