package br.com.colbert.mychart.infraestrutura.interceptors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.transaction.*;

import org.jglue.cdiunit.ProducesAlternative;
import org.junit.*;
import org.mockito.Mock;

import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes da classe {@link RequiredTransactionInterceptor}.
 * 
 * @author Thiago Colbert
 * @since 15/12/2014
 */
public class RequiredTransactionInterceptorTest extends AbstractTest {

	@Inject
	private ObjetoTeste objetoTeste;

	@Produces
	@ProducesAlternative
	@Mock
	private UserTransaction userTransaction;

	public static class ObjetoTeste {

		@Transactional
		public void testar() {
			System.out.println("[Teste] Rodou!");
		}

		@Transactional
		public void testarComException() {
			throw new IllegalArgumentException("Teste!");
		}
	}

	@Before
	public void setUp() throws SystemException {
		when(userTransaction.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION);
	}

	@Test
	public void testManageTransactionCommit() throws Exception {
		objetoTeste.testar();

		verify(userTransaction, times(1)).commit();
		verify(userTransaction, times(0)).rollback();
	}

	@Test
	public void testManageTransactionRollback() throws Exception {
		try {
			objetoTeste.testarComException();
		} catch (IllegalArgumentException exception) {
			verify(userTransaction, times(1)).rollback();
			verify(userTransaction, times(0)).commit();
		}
	}
}
