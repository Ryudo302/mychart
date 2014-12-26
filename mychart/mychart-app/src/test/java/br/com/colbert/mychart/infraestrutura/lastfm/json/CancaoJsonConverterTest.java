package br.com.colbert.mychart.infraestrutura.lastfm.json;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.Test;

import com.google.gson.JsonParseException;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link CancaoJsonConverter}.
 * 
 * @author Thiago Colbert
 * @since 25/12/2014
 */
public class CancaoJsonConverterTest extends AbstractTest {

	private static final String DIRETORIO_ARQUIVOS_JSON = "lastfm";
	private static final String NOME_ARQUIVO_JSON_CONSULTA_ARTISTA = DIRETORIO_ARQUIVOS_JSON + File.separatorChar
			+ "track-search-umbrella-rihanna.json";
	private static final String NOME_ARQUIVO_JSON_ERRO = DIRETORIO_ARQUIVOS_JSON + File.separatorChar + "error.json";

	@Inject
	private CancaoJsonConverter converter;

	@Test
	public void testFromJson() throws IOException {
		try (InputStream jsonStream = carregarArquivo(NOME_ARQUIVO_JSON_CONSULTA_ARTISTA)) {
			Collection<Cancao> cancoes = converter.fromJson(jsonStream);
			assertThat(cancoes, is(notNullValue(Collection.class)));
			assertThat(cancoes.size(), is(equalTo(1)));
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
