package br.com.colbert.mychart.dominio.artista.repository;

import java.util.Collection;

import br.com.colbert.base.dominio.*;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Repositório de {@link Artista}s.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public interface ArtistaRepository extends RepositorioIncluivelAlteravel<Artista, String>, RepositorioRemovivel<Artista, String>,
		RepositorioConsultavel<Artista, String> {

	/**
	 * Consulta por artistas que tenham o nome exatamente igual ao informado. A consulta não é <em>case-sensitive</em> (uma
	 * consulta por "FULANO" retorna os mesmos resultados que "Fulano" e "fulano").
	 * 
	 * @param nome
	 *            a ser utilizado na consulta
	 * @return os artistas que possuem o nome informado (pode estar vazia)
	 * @throws NullPointerException
	 *             caso o nome seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o nome seja uma String vazia
	 * @throws RepositoryException
	 *             caso ocorra algum erro não-tratado durante a operação
	 */
	Collection<Artista> consultarPorNomeExato(String nome) throws RepositoryException;
}
