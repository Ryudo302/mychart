package br.com.colbert.mychart.aplicacao.artista;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.jglue.cdiunit.*;
import org.junit.*;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.jpa.ArtistaJpaRepository;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.ui.artista.ArtistaView;
import br.com.colbert.mychart.ui.comum.messages.*;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes da classe {@link ConsultaArtistasWorker}.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@AdditionalClasses({ ArtistaValidador.class, ArtistaJpaRepository.class, LastFmWs.class })
public class ConsultaArtistasWorkerIT extends AbstractDbUnitTest {

	@Inject
	private ConsultaArtistasWorker worker;

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
		artistas = CollectionUtils.emptyCollection();
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

		worker.setExemplo(exemplo);
		worker.execute();

		assertThat(artistas, is(notNullValue(Collection.class)));
		assertThat(artistas.size(), is(equalTo(1)));
	}

	private void setArtistas(Collection<Artista> artistas) {
		this.artistas = artistas;
	}

	/*@Test
	public void testAdicionarNovoComArtistaExistente() {
		Artista artista = new Artista("Rihanna", TipoArtista.FEMININO_SOLO);

		controller.adicionarNovo(artista);

		verify(messages).adicionarMensagemAlerta(anyString());
	}

	@Test
	public void testAdicionarNovoComArtistaNovo() {
		Artista artista = new Artista("XXX", "Fulano", TipoArtista.MASCULINO_SOLO);

		controller.adicionarNovo(artista);

		verify(messages).adicionarMensagemSucesso(anyString());
	}

	@Test
	public void testAdicionarNovoComArtistaInvalido() {
		Artista artista = new Artista(null, null);

		controller.adicionarNovo(artista);

		verify(messages).adicionarMensagemAlerta(anyString());
	}

	@Test
	public void testRemoverArtistaExistente() {
		Artista exemplo = new Artista("Rihanna", TipoArtista.FEMININO_SOLO);
		controller.consultarExistentes(exemplo);

		Artista artista = artistas.stream().findFirst().get();
		assertThat(artista.getPersistente(), is(equalTo(true)));

		when(messages.exibirConfirmacao(anyString())).thenReturn(RespostaConfirmacao.SIM);

		controller.removerExistente(artista);

		controller.consultarExistentes(exemplo);
		assertThat(artistas.stream().findFirst().get().getPersistente(), is(equalTo(false)));
	}

	@Test
	public void testRemoverArtistaInexistente() {
		controller.removerExistente(new Artista("xxx", "Fulano", TipoArtista.MASCULINO_SOLO));

		verify(messages).adicionarMensagemAlerta(anyString());
	}*/
}
