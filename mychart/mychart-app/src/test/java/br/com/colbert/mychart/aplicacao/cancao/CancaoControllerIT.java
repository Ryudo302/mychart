package br.com.colbert.mychart.aplicacao.cancao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.doAnswer;

import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.jglue.cdiunit.*;
import org.junit.*;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.eventos.entidade.*;
import br.com.colbert.mychart.infraestrutura.jpa.CancaoJpaRepository;
import br.com.colbert.mychart.infraestrutura.lastfm.CancaoLastFmWs;
import br.com.colbert.mychart.ui.cancao.CancaoView;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes da classe {@link CancaoController}.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
@AdditionalClasses({ CancaoJpaRepository.class, CancaoLastFmWs.class })
public class CancaoControllerIT extends AbstractDbUnitTest {

	@Inject
	private CancaoController controller;

	@Produces
	@ProducesAlternative
	@Mock
	private CancaoView view;

	@Produces
	@ProducesAlternative
	@Mock
	private MessagesView messages;

	private Collection<Cancao> cancoes;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		cancoes = CollectionUtils.emptyCollection();
		doAnswer(invocation -> {
			setCancoes(invocation.getArgumentAt(0, Collection.class));
			return null;
		}).when(view).setCancoes(anyCollectionOf(Cancao.class));
	}

	private void setCancoes(Collection<Cancao> cancoes) {
		this.cancoes = cancoes;
	}

	@Override
	protected String getDataSetFileName() {
		return "cancoes-dataset.xml";
	}

	@Test
	public void testConsultarExistentes() {
		Cancao exemplo = new Cancao("Rehab", null);

		controller.consultarExistentes(new ConsultaEntidadeEvent(exemplo, ModoConsulta.TODOS));

		assertThat(cancoes, is(notNullValue(Collection.class)));
		assertThat(cancoes.size(), is(equalTo(2)));

		System.out.println(cancoes);
	}
}
