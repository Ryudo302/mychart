package br.com.colbert.base.dominio;

import java.io.Serializable;

import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Um {@link Repository} que aceita a remoção de elementos.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 * 
 * @param <E>
 *            tipo de entidade do repositório
 * @param <ID>
 *            tipo do ID das entidades
 */
public interface RepositorioRemovivel<E extends Entidade<ID>, ID extends Serializable> extends Repository<E, ID> {

	/**
	 * Remove um elemento do repositório. A tentativa de remover um artista inexistente é uma <em>no-op</em>.
	 * 
	 * @param elemento
	 *            a ser removido
	 * @return <code>true</code> caso o elemento existia no repositório, <code>false</code> caso contrário
	 * @throws NullPointerException
	 *             caso o elemento seja <code>null</code>
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	boolean remover(E elemento) throws RepositoryException;

}