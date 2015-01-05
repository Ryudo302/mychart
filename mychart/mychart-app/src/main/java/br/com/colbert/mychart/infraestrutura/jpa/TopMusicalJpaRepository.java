package br.com.colbert.mychart.infraestrutura.jpa;

import java.util.Optional;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.dominio.topmusical.repository.TopMusicalRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapper;

/**
 * Uma implementação de {@link TopMusicalRepository} que utiliza o JPA.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
public class TopMusicalJpaRepository implements TopMusicalRepository {

	@Inject
	private Logger logger;

	@Inject
	private Instance<EntityManager> entityManager;

	private EntityManager getEntityManager() {
		return entityManager.get();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao consultar top musical atual")
	public Optional<TopMusical> consultarAtual() throws RepositoryException {
		EntityManager entityManager = getEntityManager();

		logger.debug("Verificando se existe ao menos um top musical salvo.");
		return existeAoMenosUmTopMusical(entityManager) ? Optional.of((TopMusical) entityManager.createNamedQuery(
				"TopMusical.findAtual").getSingleResult()) : Optional.empty();
	}

	private boolean existeAoMenosUmTopMusical(EntityManager entityManager) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		query.select(criteriaBuilder.count(query.from(TopMusical.class)));
		Long count = entityManager.createQuery(query).getSingleResult();
		logger.debug("Quantidade de tops musicais existentes: {}", count);
		return count > 0;
	}
}
