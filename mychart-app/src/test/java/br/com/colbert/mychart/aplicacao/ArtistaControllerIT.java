package br.com.colbert.mychart.aplicacao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.doAnswer;

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
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes da classe {@link ArtistaController}.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@AdditionalClasses({ ArtistaJpaRepository.class, ArtistaLastFmRepository.class })
public class ArtistaControllerIT extends AbstractTest {

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

		// TODO Adicionar artistas no banco de testes
		System.out.println(artistas);
	}

	private void setArtistas(Collection<Artista> artistas) {
		this.artistas = artistas;
	}
}
