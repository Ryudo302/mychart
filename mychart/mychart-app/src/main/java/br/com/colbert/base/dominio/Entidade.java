package br.com.colbert.base.dominio;

import java.io.Serializable;

/**
 * Uma entidade é um objeto cujo estado é persistido em uma base de dados. Cada entidade possui um identificador único.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 *
 * @param <ID>
 *            tipo do identificador único
 */
public interface Entidade<ID extends Serializable> extends Comparable<Entidade<ID>>, Serializable {

	/**
	 * Obtém o valor do identificador único da entidade.
	 * 
	 * @return o valor do ID
	 */
	ID getId();

}