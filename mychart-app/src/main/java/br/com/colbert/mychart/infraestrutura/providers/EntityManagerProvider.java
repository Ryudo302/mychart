package br.com.colbert.mychart.infraestrutura.providers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.persistence.*;

import org.slf4j.Logger;

/**
 * Provê acesso a instâncias de {@link EntityManager}.
 * 
 * @author Thiago Colbert
 * @since 14/12/2014
 */
@ApplicationScoped
public class EntityManagerProvider {

	private static final String PERSISTENCE_UNIT_NAME = "MyChartPU";

	@Inject
	private Logger logger;

	/**
	 * Obtém uma instância de {@link EntityManagerFactory}.
	 * 
	 * @return a instância criada
	 */
	@Produces
	@ApplicationScoped
	public EntityManagerFactory entityManagerFactory() {
		logger.trace("Criando EMF a partir da persistence-unit '{}'", PERSISTENCE_UNIT_NAME);
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	/**
	 * Fecha uma {@link EntityManagerFactory}.
	 * 
	 * @param factory
	 *            a ser fechado
	 */
	public void closeEntityManagerFactory(@Disposes EntityManagerFactory factory) {
		if (factory.isOpen()) {
			logger.trace("Fechando EMF");
			factory.close();
		}
	}

	/**
	 * Obtém uma instância de {@link EntityManager}.
	 * 
	 * @param factory
	 *            fábrica a ser utilizada para criar o {@link EntityManager}
	 * @return a instância criada
	 */
	@Produces
	public EntityManager entityManager(EntityManagerFactory factory) {
		logger.trace("Criando novo EM a partir da factory: {}", factory);
		return factory.createEntityManager();
	}

	/**
	 * Fecha um {@link EntityManager}.
	 * 
	 * @param entityManager
	 *            a ser fechado
	 */
	public void closeEntityManager(@Disposes EntityManager entityManager) {
		if (entityManager.isOpen()) {
			logger.trace("Fechando EM: {}", entityManager);
			entityManager.close();
		}
	}
}
