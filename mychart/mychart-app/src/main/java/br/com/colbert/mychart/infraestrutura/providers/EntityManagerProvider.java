package br.com.colbert.mychart.infraestrutura.providers;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

/**
 * Provê acesso a instâncias de {@link EntityManager}.
 * 
 * @author Thiago Colbert
 * @since 14/12/2014
 */
@ApplicationScoped
public class EntityManagerProvider implements Serializable {

	private static final long serialVersionUID = -297186060011341965L;

	private static final String PERSISTENCE_UNIT_NAME = "MyChartPU";

	@Inject
	private transient Logger logger;

	// TODO Está aqui para garantir que o gerenciador de transações seja iniciado antes da EMF
	@SuppressWarnings("unused")
	@Inject
	private transient UserTransaction transaction;

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
