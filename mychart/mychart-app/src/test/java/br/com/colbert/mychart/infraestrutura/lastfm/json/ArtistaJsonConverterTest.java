package br.com.colbert.mychart.infraestrutura.lastfm.json;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.Test;

import com.google.gson.JsonParseException;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link ArtistaJsonConverter}.
 * 
 * @author Thiago Colbert
 * @since 25/12/2014
 */
public class ArtistaJsonConverterTest extends AbstractTest {

	private static final String DIRETORIO_ARQUIVOS_JSON = "lastfm";
	private static final String NOME_ARQUIVO_JSON_CONSULTA_ARTISTA = DIRETORIO_ARQUIVOS_JSON + File.separatorChar
			+ "artist-search-rihanna.json";
	private static final String NOME_ARQUIVO_JSON_ERRO = DIRETORIO_ARQUIVOS_JSON + File.separatorChar + "error.json";

	@Inject
	private ArtistaJsonConverter converter;

	@Test
	public void testFromJson() throws IOException {
		try (InputStream jsonStream = carregarArquivo(NOME_ARQUIVO_JSON_CONSULTA_ARTISTA)) {
			Collection<Artista> artistas = converter.fromJson(jsonStream);
			assertThat(artistas, is(notNullValue(Collection.class)));
			assertThat(artistas.size(), is(equalTo(1)));

		}
	}

	@Test(expected = JsonParseException.class)
	public void testFromJsonInvalido() throws IOException {
		try (InputStream jsonStream = carregarArquivo(NOME_ARQUIVO_JSON_ERRO)) {
			converter.fromJson(jsonStream);
		}
	}

	private InputStream carregarArquivo(String nome) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(nome);
	}
}
