package br.com.colbert.mychart.infraestrutura.lastfm;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.*;
import java.util.*;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.ProducesAlternative;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.mychart.infraestrutura.lastfm.api.*;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link ArtistaLastFmWs}.
 * 
 * @author Thiago Colbert
 * @since 25/12/2014
 */
public class ArtistaLastFmWsTest extends AbstractTest {

	private static final String DIRETORIO_ARQUIVOS_JSON = "lastfm";
	private static final String NOME_ARQUIVO_JSON_CONSULTA_ARTISTA = DIRETORIO_ARQUIVOS_JSON + File.separatorChar
			+ "artist-search-rihanna.json";
	private static final String NOME_ARQUIVO_JSON_ERRO = DIRETORIO_ARQUIVOS_JSON + File.separatorChar + "error.json";

	@Inject
	private ArtistaLastFmWs artistaWs;

	@Produces
	@ProducesAlternative
	@Mock
	private LastFmWs lastFmWs;

	@SuppressWarnings("unchecked")
	@Test
	public void testConsultarPor() throws ServiceException, LastFmException {
		InputStream stream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(NOME_ARQUIVO_JSON_CONSULTA_ARTISTA);
		when(lastFmWs.executarOperacao(any(Metodo.class), any(Map.class))).thenReturn(stream);

		Collection<Artista> artistas = artistaWs.consultarPor(new Artista("rihanna", TipoArtista.FEMININO_SOLO));

		assertThat(artistas, is(notNullValue(Collection.class)));
		assertThat(artistas.size(), is(equalTo(1)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testConsultaComRespostaDeErro() throws LastFmException, ServiceException {
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(NOME_ARQUIVO_JSON_ERRO);
		when(lastFmWs.executarOperacao(any(Metodo.class), any(Map.class))).thenReturn(stream);

		try {
			artistaWs.consultarPor(new Artista("rihanna", TipoArtista.FEMININO_SOLO));
			fail("Deveria lançar exceção");
		} catch (ServiceException exception) {
			Throwable cause = exception.getCause();
			assertThat(cause.getClass(), is(equalTo(LastFmException.class)));
		}
	}
}
