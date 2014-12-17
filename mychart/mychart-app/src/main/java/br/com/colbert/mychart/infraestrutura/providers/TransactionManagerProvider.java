package br.com.colbert.mychart.infraestrutura.providers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

import bitronix.tm.TransactionManagerServices;

/**
 * Provê acesso a instâncias responsáveis por gerenciamento de transações (JTA).
 * 
 * @author Thiago Colbert
 * @since 15/12/2014
 */
@ApplicationScoped
public class TransactionManagerProvider implements Serializable {

	private static final long serialVersionUID = 1466564838332321639L;

	private static final String DATASOURCE_CONFIG_FILENAME;

	static {
		DATASOURCE_CONFIG_FILENAME = Thread.currentThread().getContextClassLoader().getResource("datasources.properties")
				.toExternalForm().replaceFirst("[a-z]+:/", "");
	}

	@Inject
	private transient Logger logger;

	@PostConstruct
	public void init() {
		TransactionManagerServices.getConfiguration().setResourceConfigurationFilename(DATASOURCE_CONFIG_FILENAME);
		logger.trace("Configurações carregadas");
	}

	/**
	 * Obtém uma instância de {@link UserTransaction}.
	 * 
	 * @return a instância criada
	 */
	@Produces
	public UserTransaction userTransaction() {
		logger.trace("Obtendo Transaction Manager");
		return TransactionManagerServices.getTransactionManager();
	}

	/**
	 * Libera os recursos utilizados pelo gerenciador de transações
	 * 
	 * @param userTransaction
	 */
	public void closeTransactionManager(@Disposes UserTransaction userTransaction) {
		logger.trace("Shutdown do Transaction Manager");
		TransactionManagerServices.getTransactionManager().shutdown();
	}
}
