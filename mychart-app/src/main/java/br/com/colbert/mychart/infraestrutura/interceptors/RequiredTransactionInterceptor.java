package br.com.colbert.mychart.infraestrutura.interceptors;

import static javax.transaction.Transactional.TxType.REQUIRED;

import javax.inject.Inject;
import javax.interceptor.*;
import javax.transaction.*;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;

/**
 * {@link Interceptor} responsável pelo gerenciamento de transações de métodos marcados com {@link Transactional} e com o tipo
 * {@link TxType#REQUIRED}.
 * 
 * @author Thiago Colbert
 * @since 15/12/2014
 */
@Transactional(REQUIRED)
@Interceptor
public class RequiredTransactionInterceptor {

	@Inject
	private UserTransaction transaction;

	@Inject
	private Logger logger;

	/**
	 * Gerencia uma transação para a execução do método.
	 * 
	 * @param context
	 *            contexto de invocação do método
	 * @return resultado da execução do método gerenciando
	 * @throws Exception
	 *             caso ocorra algum erro durante a execução do método ou no gerenciamento de transação
	 */
	@AroundInvoke
	public Object manageTransaction(InvocationContext context) throws Exception {
		if (transaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
			logger.debug("Nenhuma transação ativa - iniciando uma nova");

			transaction.begin();

			try {
				Object result = context.proceed();
				logger.debug("COMMIT");
				transaction.commit();
				return result;
			} catch (Exception exception) {
				logger.debug("ROLLBACK");
				transaction.rollback();
				throw exception;
			}
		} else {
			logger.debug("Reutilizando a transação que já está ativa");
			return context.proceed();
		}
	}
}
