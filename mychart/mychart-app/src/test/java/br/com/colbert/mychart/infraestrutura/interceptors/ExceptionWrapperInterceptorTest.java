package br.com.colbert.mychart.infraestrutura.interceptors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes da classe {@link ExceptionWrapperInterceptor}.
 * 
 * @author Thiago Colbert
 * @since 15/12/2014
 */
public class ExceptionWrapperInterceptorTest extends AbstractTest {

	@Inject
	private ObjetoTeste objetoTeste;

	public static class ObjetoTeste {

		@ExceptionWrapper(de = NullPointerException.class, para = IllegalArgumentException.class, mensagem = "Teste com {0}")
		public void testar(String valor) {
			throw new NullPointerException();
		}
	}

	@Test
	public void testWrapException() {
		try {
			objetoTeste.testar("Teste");
			fail("Deveria lançar uma IllegalArgumentException");
		} catch (IllegalArgumentException exception) {
			assertThat(exception.getCause(), instanceOf(NullPointerException.class));
			assertThat(exception.getLocalizedMessage(), is(equalTo("Teste com Teste")));
		}
	}
}
