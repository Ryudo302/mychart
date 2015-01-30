package br.com.colbert.mychart.aplicacao.topmusical;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.ProducesAlternative;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.dominio.topmusical.TopMusicalConfiguration;
import br.com.colbert.mychart.infraestrutura.jpa.TopMusicalJpaRepository;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.topmusical.PrimeiroTopMusicalView;
import br.com.colbert.mychart.ui.topmusical.TopMusicalPanel;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes de integração da {@link TopMusicalPresenter}.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@AdditionalClasses({ TopMusicalConfiguration.class, TopMusicalJpaRepository.class, PrimeiroTopMusicalView.class })
public class TopMusicalPresenterIT extends AbstractDbUnitTest {

	@Inject
	private TopMusicalPresenter presenter;

	@Produces
	@ProducesAlternative
	@Mock
	private TopMusicalPanel view;

	@Produces
	@ProducesAlternative
	@Mock
	private MessagesView messages;

	private TopMusical topMusical;

	@Before
	public void setUp() {

	}

	@Override
	protected String getDataSetFileName() {
		return "top-musical-dataset.xml";
	}

	@Test
	@Ignore
	public void testCarregarTopAtual() {
		// TODO 
		
		presenter.start();

		assertThat(topMusical, is(notNullValue()));
		assertThat(topMusical.getNumero(), is(equalTo(2)));
	}
}
