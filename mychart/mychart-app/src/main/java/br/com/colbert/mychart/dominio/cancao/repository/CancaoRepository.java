package br.com.colbert.mychart.dominio.cancao.repository;

import java.util.Collection;

import br.com.colbert.base.dominio.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.exception.*;

/**
 * Repositório de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public interface CancaoRepository extends RepositorioAdicionavel<Cancao, String>, RepositorioRemovivel<Cancao, String> {

	/**
	 * Consulta por canções que tenham o título e artista(s) exatamente iguais aos informados. A consulta não é
	 * <em>case-sensitive</em> (uma consulta pelo título "TESTE" retorna os mesmos resultados que "Teste" e "teste").
	 * 
	 * @param exemplo
	 *            a ser utilizado na consulta
	 * @return as canções que possuem o título e artistas informados (pode estar vazia)
	 * @throws NullPointerException
	 *             caso o o exemplo seja <code>null</code>
	 * @throws RepositoryException
	 *             caso ocorra algum erro não-tratado durante a operação
	 */
	Collection<Cancao> consultarPorTituloEArtistaExatos(Cancao exemplo) throws RepositoryException;

	/**
	 * Faz uma consulta por canções a partir de uma canção de exemplo.
	 * 
	 * @param exemplo
	 *            a ser utilizada na consulta
	 * @return as canções encontradas (pode ser uma lista vazia)
	 * @throws NullPointerException
	 *             caso o o exemplo seja <code>null</code>
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	Collection<Cancao> consultarPor(Cancao exemplo) throws RepositoryException;

	/**
	 * Altera uma canção existente no repositório.
	 * 
	 * @param cancao
	 *            a ser alterada
	 * @throws NullPointerException
	 *             caso o a canção seja <code>null</code>
	 * @throws ElementoNaoExistenteException
	 *             caso a canção informada não exista no repositório
	 * @throws RepositoryException
	 *             caso ocorra algum erro não tratado durante a operação
	 */
	void alterar(Cancao cancao) throws ElementoNaoExistenteException, RepositoryException;
}
