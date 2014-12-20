package br.com.colbert.mychart.aplicacao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.*;
import org.junit.*;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaJpaRepository;
import br.com.colbert.mychart.infraestrutura.lastfm.ArtistaLastFmRepository;
import br.com.colbert.mychart.ui.artista.ArtistaView;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes da classe {@link ArtistaController}.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@AdditionalClasses({ ArtistaJpaRepository.class, ArtistaLastFmRepository.class })
public class ArtistaControllerIT extends AbstractDbUnitTest {

	@Inject
	private ArtistaController controller;

	@Produces
	@ProducesAlternative
	@Mock
	private ArtistaView view;

	@Produces
	@ProducesAlternative
	@Mock
	private MessagesView messages;

	private Collection<Artista> artistas;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		doAnswer(invocation -> {
			setArtistas(invocation.getArgumentAt(0, Collection.class));
			return null;
		}).when(view).setArtistas(anyCollectionOf(Artista.class));
	}

	@Override
	protected String getDataSetFileName() {
		return "artistas-dataset.xml";
	}

	@Test
	public void testConsultarExistentes() {
		Artista exemplo = new Artista("rihanna", TipoArtista.FEMININO_SOLO);

		controller.consultarExistentes(exemplo);

		assertThat(artistas, is(notNullValue(Collection.class)));
		assertThat(artistas.size(), is(equalTo(2)));

		System.out.println(artistas);
	}

	private void setArtistas(Collection<Artista> artistas) {
		this.artistas = artistas;
	}

	@Test
	public void testAdicionarNovaComArtistaExistente() {
		Artista artista = new Artista("Rihanna", TipoArtista.FEMININO_SOLO);

		controller.adicionarNova(artista);

		verify(messages).adicionarMensagemAlerta(anyString());
	}

	@Test
	public void testAdicionarNovaComArtistaNovo() {
		Artista artista = new Artista("Fulano", TipoArtista.MASCULINO_SOLO);

		controller.adicionarNova(artista);

		verify(messages).adicionarMensagemSucesso(anyString());
	}

	@Test
	public void testRemoverArtistaExistente() {
		Artista exemplo = new Artista("Rihanna", TipoArtista.FEMININO_SOLO);
		controller.consultarExistentes(exemplo);

		controller.removerExistente(artistas.stream().filter(artista -> artista.getId() != null).findFirst().get());
		controller.consultarExistentes(exemplo);
		
		System.out.println(artistas);

		assertThat(artistas.size(), is(equalTo(1)));
	}

	@Test
	public void testRemoverArtistaInexistente() {
		controller.removerExistente(new Artista("Fulano", TipoArtista.MASCULINO_SOLO));

		controller.consultarExistentes(new Artista(null, null));
		
		System.out.println(artistas);
		
		assertThat(artistas.size(), is(equalTo(2)));
	}
}
