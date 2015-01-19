package br.com.colbert.mychart.aplicacao.topmusical;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

import java.util.Optional;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.*;
import org.junit.*;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.infraestrutura.jpa.TopMusicalJpaRepository;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.topmusical.*;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes de integração da {@link TopMusicalController}.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@AdditionalClasses({ TopMusicalConfiguration.class, TopMusicalJpaRepository.class, PrimeiroTopMusicalView.class })
public class TopMusicalControllerIT extends AbstractDbUnitTest {

	@Inject
	private TopMusicalController controller;

	@Produces
	@ProducesAlternative
	@Mock
	private TopMusicalView view;

	@Produces
	@ProducesAlternative
	@Mock
	private MessagesView messages;

	private TopMusical topMusical;

	@Before
	public void setUp() {
		doAnswer(invocation -> {
			topMusical = (TopMusical) invocation.getArgumentAt(0, Optional.class).get();
			return null;
		}).when(view).setTopMusical(any());
	}

	@Override
	protected String getDataSetFileName() {
		return "top-musical-dataset.xml";
	}

	@Test
	public void testCarregarTopAtual() {
		controller.carregarTopAtual(view);

		assertThat(topMusical, is(notNullValue()));
		assertThat(topMusical.getNumero(), is(equalTo(2)));
	}
}
