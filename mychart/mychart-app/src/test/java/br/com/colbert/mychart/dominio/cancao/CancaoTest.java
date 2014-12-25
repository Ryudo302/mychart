package br.com.colbert.mychart.dominio.cancao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Testes unitários da classe {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class CancaoTest {

	private Cancao cancao;

	@Mock
	private Artista artista1;
	@Mock
	private Artista artista2;

	@Before
	public void setUp() {
		when(artista1.getId()).thenReturn(1);
		when(artista1.getNome()).thenReturn("A");

		when(artista2.getNome()).thenReturn("B");
		when(artista2.getId()).thenReturn(2);
	}

	@Test
	public void deveriaCriarListaDeArtistasCancaoMantendoAOrdemOriginal() {
		cancao = new Cancao(1, "Teste", artista1, artista2);

		List<ArtistaCancao> artistasCancao = cancao.getArtistasCancao();

		assertThat(artistasCancao, is(notNullValue(List.class)));
		assertThat(artistasCancao.size(), is(equalTo(2)));
	}

	@Test
	public void testGetArtistaPrincipalDeCancaoSemArtistas() {
		cancao = new Cancao();

		Optional<Artista> artistaPrincipal = cancao.getArtistaPrincipal();

		assertThat(artistaPrincipal.isPresent(), is(false));
	}

	@Test
	public void testGetArtistaPrincipalDeCancaoCom1Artista() {
		cancao = new Cancao("Teste", artista1);

		Optional<Artista> artistaPrincipal = cancao.getArtistaPrincipal();

		assertThat(artistaPrincipal.isPresent(), is(true));
		assertThat(artistaPrincipal.get(), is(equalTo(artista1)));
	}

	@Test
	public void testGetArtistaPrincipalDeCancaoComVariosArtista() {
		cancao = new Cancao("Teste", artista1, artista2);

		Optional<Artista> artistaPrincipal = cancao.getArtistaPrincipal();

		assertThat(artistaPrincipal.isPresent(), is(true));
		assertThat(artistaPrincipal.get(), is(equalTo(artista1)));
	}
}
