package br.com.colbert.tests.support;

import org.jboss.weld.log.LoggerProducer;
import org.jglue.cdiunit.*;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import br.com.colbert.mychart.infraestrutura.info.ApplicationProperties;
import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapperInterceptor;
import br.com.colbert.mychart.infraestrutura.providers.TransactionManagerProvider;

/**
 * Classe-base para todos os testes da aplicação.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@RunWith(CdiRunner.class)
@AdditionalPackages({ ExceptionWrapperInterceptor.class, TransactionManagerProvider.class, ApplicationProperties.class })
@AdditionalClasses(LoggerProducer.class)
public abstract class AbstractTest {

	@Before
	public void setUpContext() {
		// O CDI-Unit muda essa propriedade e causa erro no Hibernate
		System.setProperty("java.naming.factory.initial", "bitronix.tm.jndi.BitronixInitialContextFactory");
	}

	@Rule
	public TestRule printTestName() {
		return new PrintTestNameWatcher();
	}
}