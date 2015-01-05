package br.com.colbert.base.dominio;

import java.io.Serializable;

import br.com.colbert.mychart.infraestrutura.exception.*;

/**
 * Um {@link Repository} que aceita a inclusão de elementos.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 * 
 * @param <E>
 *            tipo de entidade do repositório
 * @param <ID>
 *            tipo do ID das entidades
 */
public interface RepositorioAdicionavel<E extends Entidade<ID>, ID extends Serializable> extends Repository<E, ID> {

	/**
	 * Adiciona um novo elemento ao repositório.
	 * 
	 * @param elemento
	 *            a ser adicionado
	 * @throws NullPointerException
	 *             caso o elemento seja <code>null</code>
	 * @throws ElementoJaExistenteException
	 *             caso o elemento informado já exista no repositório
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	void adicionar(E elemento) throws ElementoJaExistenteException, RepositoryException;
}