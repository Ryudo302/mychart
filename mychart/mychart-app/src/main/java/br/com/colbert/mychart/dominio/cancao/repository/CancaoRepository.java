package br.com.colbert.mychart.dominio.cancao.repository;

import java.util.Collection;

import br.com.colbert.base.dominio.Repository;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.exception.*;

/**
 * Repositório local de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public interface CancaoRepository extends Repository<Cancao, Integer> {

	/**
	 * Consulta por canções que tenham o título exatamente igual ao informado. A consulta não é <em>case-sensitive</em> (uma
	 * consulta por "TESTE" retorna os mesmos resultados que "Teste" e "teste").
	 * 
	 * @param titulo
	 *            a ser utilizado na consulta
	 * @return as canções que possuem o título informado (pode estar vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro não-tratado durante a operação
	 */
	Collection<Cancao> consultarPorTituloExato(String titulo) throws RepositoryException;

	/**
	 * Faz uma consulta por canções a partir de uma canção de exemplo.
	 * 
	 * @param exemplo
	 *            a ser utilizada na consulta
	 * @return as canções encontradas (pode ser uma lista vazia)
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	Collection<Cancao> consultarPor(Cancao exemplo) throws RepositoryException;

	/**
	 * Adiciona uma nova canção ao repositório.
	 * 
	 * @param cancao
	 *            a ser adicionada
	 * @throws ElementoJaExistenteException
	 *             caso a canção informada já exista no repositório
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	void adicionar(Cancao cancao) throws ElementoJaExistenteException, RepositoryException;

	/**
	 * Altera uma canção existente no repositório.
	 * 
	 * @param cancao
	 *            a ser alterada
	 * @throws ElementoNaoExistenteException
	 *             caso a canção informada não exista no repositório
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	void alterar(Cancao cancao) throws ElementoNaoExistenteException, RepositoryException;

	/**
	 * Remove uma canção do repositório. A tentativa de remover uma canção inexistente é uma <em>no-op</em>.
	 * 
	 * @param cancao
	 *            a ser removida
	 * @return <code>true</code> caso a canção existia no repositório, <code>false</code> caso contrário
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	boolean remover(Cancao cancao) throws RepositoryException;
}
