package br.com.colbert.base.dominio;

import java.io.Serializable;

import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Um {@link Repository} que aceita a inclusão e alteração de elementos.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 * 
 * @param <E>
 *            tipo de entidade do repositório
 * @param <ID>
 *            tipo do ID das entidades
 */
public interface RepositorioIncluivelAlteravel<E extends Entidade<ID>, ID extends Serializable> extends Repository<E, ID> {

	/**
	 * Adiciona um novo elemento ou atualiza um elemento existente no repositório.
	 * 
	 * @param elemento
	 *            a ser adicionado ou alterado
	 * @return o elemento que foi incluído ou alterado
	 * @throws NullPointerException
	 *             caso o elemento seja <code>null</code>
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	E incluirOuAlterar(E elemento) throws RepositoryException;
}