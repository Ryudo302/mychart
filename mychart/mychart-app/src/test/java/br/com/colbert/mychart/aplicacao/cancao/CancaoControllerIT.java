package br.com.colbert.mychart.aplicacao.cancao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.util.*;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.jglue.cdiunit.*;
import org.junit.*;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.jpa.CancaoJpaRepository;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.ui.cancao.CancaoView;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes da classe {@link CancaoController}.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
@AdditionalClasses({ CancaoValidador.class, CancaoJpaRepository.class, LastFmWs.class })
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

	private Artista artistaExistente;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		cancoes = CollectionUtils.emptyCollection();
		doAnswer(invocation -> {
			setCancoes(invocation.getArgumentAt(0, Collection.class));
			return null;
		}).when(view).setCancoes(anyCollectionOf(Cancao.class));

		artistaExistente = entityManager.find(Artista.class, "2");
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
		Cancao exemplo = new Cancao("rehab", (List<Artista>) null);

		controller.consultarExistentes(exemplo);

		assertThat(cancoes, is(notNullValue(Collection.class)));
		assertThat(cancoes.size(), is(equalTo(2)));
	}

	@Test
	public void testAdicionarNovaComCancaoExistente() {
		Cancao cancao = new Cancao("xxx", "Unfaithful", artistaExistente);

		controller.adicionarNova(cancao);

		verify(messages).adicionarMensagemAlerta(anyString());
	}

	@Test
	public void testAdicionarNovaComCancaoNovo() {
		Cancao cancao = new Cancao("xxx", "Rude Boy", artistaExistente);

		controller.adicionarNova(cancao);

		verify(messages).adicionarMensagemSucesso(anyString());
	}

	@Test
	public void testAdicionarNovaComCancaoInvalida() {
		Cancao cancao = new Cancao(null, (List<Artista>) null);

		controller.adicionarNova(cancao);

		verify(messages).adicionarMensagemAlerta(anyString());
	}
}
