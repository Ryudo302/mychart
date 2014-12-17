package br.com.colbert.base.ui;

import java.util.Collection;

import br.com.colbert.base.dominio.Entidade;

/**
 * Uma view de entidades.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 *
 * @param <T>
 *            tipo de entidade
 */
public interface EntidadeView<T extends Entidade<?>> {

	/**
	 * Define a entidade atualmente sendo exibida em detalhes na visão.
	 * 
	 * @param entidade
	 */
	void setEntidadeAtual(T entidade);

	/**
	 * Define as entidades a serem listadas na visão.
	 * 
	 * @param entidades
	 */
	void setEntidades(Collection<T> entidades);
}
