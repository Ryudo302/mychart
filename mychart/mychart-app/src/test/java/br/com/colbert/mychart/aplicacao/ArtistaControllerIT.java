package br.com.colbert.mychart.aplicacao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.*;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaJpaRepository;
import br.com.colbert.mychart.infraestrutura.lastfm.ArtistaLastFmRepository;
import br.com.colbert.mychart.ui.MessagesView;
import br.com.colbert.mychart.ui.artista.ArtistaView;
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

	@Override
	protected String getDataSetFileName() {
		return "artistas-dataset.xml";
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testConsultarExistentes() {
		doAnswer(invocation -> {
			setArtistas(invocation.getArgumentAt(0, Collection.class));
			return null;
		}).when(view).setEntidades(anyCollectionOf(Artista.class));

		Artista exemplo = new Artista("rihanna", TipoArtista.FEMININO_SOLO);

		controller.consultarExistentes(exemplo);

		assertThat(artistas, is(notNullValue(Collection.class)));
		assertThat(artistas.size() > 0, is(true));

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
}
