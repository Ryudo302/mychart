package br.com.colbert.mychart.dominio.topmusical.repository;

import java.util.Optional;

import br.com.colbert.base.dominio.*;
import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;

/**
 * Repositório de {@link TopMusical}.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
public interface TopMusicalRepository extends RepositorioIncluivelAlteravel<TopMusical, Integer>,
		RepositorioRemovivel<TopMusical, Integer>, RepositorioConsultavel<TopMusical, Integer> {

	/**
	 * Consulta o {@link TopMusical} mais atual, que é aquele cuja data seja a mais atual.
	 * 
	 * @return o top musical atual
	 * @throws RepositoryException
	 *             caso ocorra algum erro durante a consulta
	 */
	Optional<TopMusical> consultarAtual() throws RepositoryException;
}
