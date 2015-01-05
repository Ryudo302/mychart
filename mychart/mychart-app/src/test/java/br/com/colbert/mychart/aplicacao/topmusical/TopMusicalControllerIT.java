package br.com.colbert.mychart.aplicacao.topmusical;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.*;
import org.junit.*;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.infraestrutura.jpa.TopMusicalJpaRepository;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.topmusical.TopMusicalView;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes de integração da {@link TopMusicalController}.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@AdditionalClasses({ TopMusicalConfiguration.class, TopMusicalJpaRepository.class })
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
			topMusical = invocation.getArgumentAt(0, TopMusical.class);
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
