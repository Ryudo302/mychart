package br.com.colbert.base.dominio;

import java.io.Serializable;
import java.util.Collection;

import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Um {@link Repository} que permite a consulta por elementos.
 * 
 * @author Thiago Colbert
 * @since 20/01/2015
 *
 * @param <E>
 *            tipo de entidade do repositório
 * @param <ID>
 *            tipo do ID das entidades
 */
public interface RepositorioConsultavel<E extends Entidade<ID>, ID extends Serializable> extends Repository<E, ID> {

	/**
	 * Faz uma consulta por elementos a partir de um exemplo.
	 * 
	 * @param exemplo
	 *            a ser utilizado na consulta
	 * @return as entidades encontradas (pode ser uma lista vazia)
	 * @throws NullPointerException
	 *             caso o exemplo seja <code>null</code>
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	Collection<E> consultarPor(E exemplo) throws RepositoryException;
}