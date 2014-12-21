package br.com.colbert.mychart.dominio.artista.repository;

import java.util.Collection;

import br.com.colbert.base.dominio.Repository;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.exception.*;

/**
 * Repositório local de {@link Artista}s. Ele difere de um repositório remoto por permitir operações de inserção, remoção e/ou
 * atualização de elementos.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 * @see ArtistaRepositoryRemoto
 */
public interface ArtistaRepositoryLocal extends Repository<Artista, Integer> {

	/**
	 * Consulta por artistas que tenham o nome exatamente igual ao informado. A consulta não é <em>case-sensitive</em> (uma
	 * consulta por "FULANO" retorna os mesmos resultados que "Fulano" e "fulano").
	 * 
	 * @param nome
	 *            a ser utilizado na consulta
	 * @return os artistas que possuem o nome informado (pode estar vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro não-tratado durante a operação
	 */
	Collection<Artista> consultarPorNomeExato(String nome) throws RepositoryException;

	/**
	 * Faz uma consulta por artistas a partir de um artista de exemplo.
	 * 
	 * @param exemplo
	 *            a ser utilizada na consulta
	 * @return os artistas encontradas (pode ser uma lista vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	Collection<Artista> consultarPor(Artista exemplo) throws RepositoryException;

	/**
	 * Adiciona um novo artista ao repositório.
	 * 
	 * @param artista
	 *            a ser adicionado
	 * @throws ElementoJaExistenteException
	 *             caso o artista informado já exista no repositório
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	void adicionar(Artista artista) throws ElementoJaExistenteException, RepositoryException;

	/**
	 * Remove um artista do repositório. A tentativa de remover um artista inexistente é uma <em>no-op</em>.
	 * 
	 * @param artista
	 *            a ser removido
	 * @return <code>true</code> caso o artista existia no repositório, <code>false</code> caso contrário
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	boolean remover(Artista artista) throws RepositoryException;
}
