package br.com.colbert.tests.support;

import org.jboss.weld.log.LoggerProducer;
import org.jglue.cdiunit.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapperInterceptor;
import br.com.colbert.mychart.infraestrutura.providers.TransactionManagerProvider;

/**
 * Classe-base para todos os testes da aplicação.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@RunWith(CdiRunner.class)
@AdditionalPackages({ ExceptionWrapperInterceptor.class, TransactionManagerProvider.class })
@AdditionalClasses(LoggerProducer.class)
public abstract class AbstractTest {

	@Rule
	public TestRule printTestName() {
		return new PrintTestNameWatcher();
	}
}