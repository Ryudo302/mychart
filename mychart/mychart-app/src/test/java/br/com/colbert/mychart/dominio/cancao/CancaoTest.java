package br.com.colbert.mychart.dominio.cancao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.junit.Test;

import br.com.colbert.mychart.dominio.artista.*;

/**
 * Testes unitários da classe {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
public class CancaoTest {

	private Cancao cancao;

	@Test
	public void deveriaCriarListaDeArtistasCancaoMantendoAOrdemOriginal() {
		final String nomeArtista1 = "A";
		final String nomeArtista2 = "B";

		Artista artista1 = new Artista(nomeArtista1, TipoArtista.DESCONHECIDO);
		Artista artista2 = new Artista(nomeArtista2, TipoArtista.DESCONHECIDO);

		cancao = new Cancao("Teste", Arrays.asList(artista1, artista2));

		List<ArtistaCancao> artistasCancao = cancao.getArtistasCancao();

		assertThat(artistasCancao, is(notNullValue(List.class)));
		assertThat(artistasCancao.size(), is(equalTo(2)));

		ArtistaCancao artistaCancao1 = artistasCancao.get(0);
		assertThat(artistaCancao1.getArtista().getNome(), is(equalTo(nomeArtista1)));
		assertThat(artistaCancao1.getOrdem(), is(equalTo(0)));

		ArtistaCancao artistaCancao2 = artistasCancao.get(1);
		assertThat(artistaCancao2.getArtista().getNome(), is(equalTo(nomeArtista2)));
		assertThat(artistaCancao2.getOrdem(), is(equalTo(1)));
	}
}
