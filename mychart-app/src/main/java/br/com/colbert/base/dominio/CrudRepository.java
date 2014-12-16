package br.com.colbert.base.dominio;

import java.io.Serializable;
import java.util.Collection;

import br.com.colbert.mychart.infraestrutura.exception.*;

/**
 * Um repositório de entidades que fornece operações CRUD.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 *
 * @param <T>
 *            tipo de entidade
 * @param <ID>
 *            tipo do identificador único da entidade
 */
public interface CrudRepository<T extends Entidade<ID>, ID extends Serializable> extends Repository<T, ID> {

	/**
	 * Faz uma consulta por entidades a partir de um entidade de exemplo.
	 * 
	 * @param exemplo
	 *            a ser utilizada na consulta
	 * @return as entidades encontradas (pode ser uma lista vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	Collection<T> consultarPor(T exemplo) throws RepositoryException;

	/**
	 * Adiciona uma nova entidade ao repositório.
	 * 
	 * @param entidade
	 *            a ser adicionado
	 * @throws ElementoJaExistenteException
	 *             caso o entidade informado já exista no repositório
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	void adicionar(T entidade) throws ElementoJaExistenteException, RepositoryException;

	/**
	 * Atualiza um entidade já presente no repositório.
	 * 
	 * @param entidade
	 *            a ser atualizada
	 * @return a entidade que foi atualizada
	 * @throws ElementoNaoExistenteException
	 *             caso o entidade informado não exista no repositório
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	T atualizar(T entidade) throws ElementoNaoExistenteException, RepositoryException;

	/**
	 * Remove uma entidade do repositório. A tentativa de remover uma entidade inexistente é uma <em>no-op</em>.
	 * 
	 * @param entidade
	 *            a ser removida
	 * @return <code>true</code> caso a entidade existia no repositório, <code>false</code> caso contrário
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	boolean remover(T entidade) throws RepositoryException;
}
