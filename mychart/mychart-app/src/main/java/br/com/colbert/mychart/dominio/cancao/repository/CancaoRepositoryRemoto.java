package br.com.colbert.mychart.dominio.cancao.repository;

import java.util.Collection;

import br.com.colbert.base.dominio.Repository;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Repositório remoto de {@link Cancao}. Ele difere de um repositório local por ser <em>read-only</em>.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 * @see CancaoRepositoryLocal
 */
public interface CancaoRepositoryRemoto extends Repository<Cancao, Integer> {

	/**
	 * Faz uma consulta por canções a partir de seu título.
	 * 
	 * @param titulo
	 *            a ser utilizado na consulta
	 * @return as canções encontradas (pode ser uma lista vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro durante a operação
	 */
	Collection<? extends Cancao> consultarPor(String titulo) throws RepositoryException;
}
