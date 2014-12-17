package br.com.colbert.tests.support;

import org.jglue.cdiunit.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapperInterceptor;
import br.com.colbert.mychart.infraestrutura.providers.LoggerProvider;

/**
 * Classe-base para todos os testes da aplicação.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@RunWith(CdiRunner.class)
@AdditionalPackages({ LoggerProvider.class, ExceptionWrapperInterceptor.class })
public abstract class AbstractTest {

	@Rule
	public TestRule printTestName() {
		return new PrintTestNameWatcher();
	}
}