package br.com.colbert.mychart.infraestrutura.io.parser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da {@link ArtistaParser}.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 */
public class ArtistaParserTest extends AbstractTest {

	@Inject
	private ArtistaParser parser;

	@Test
	public void testConvertString() throws IOException {
		String linha = "1 A feat. B & C - \"Teste\" [ NEW ] [1ª Semana] [PP:1]";

		List<Artista> artistas = parser.parse(linha);

		assertThat(artistas, is(notNullValue(List.class)));
		assertThat(artistas.size(), is(equalTo(3)));
	}
}
