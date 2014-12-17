package br.com.colbert.mychart.infraestrutura.formatter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.*;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.tests.support.AbstractTest;

;

/**
 * Testes da classe {@link CancaoFormatter}.
 * 
 * @author Thiago Colbert
 * @since 10/12/2014
 */
public class CancaoFormatterTest extends AbstractTest {

	private static final String NOME_ARTISTA = "Fulano";
	private static final String TITULO_CANCAO = "Teste";

	@Inject
	private CancaoFormatter formatter;

	private Artista artista;

	@Before
	public void setUp() {
		artista = new Artista(NOME_ARTISTA, TipoArtista.MASCULINO_SOLO);
	}

	@Test
	public void testFormat() {
		Cancao cancao = new Cancao(TITULO_CANCAO, Arrays.asList(artista, artista, artista, artista, artista));

		String cancaoFormatada = formatter.format(cancao);
		System.out.println(cancaoFormatada);

		assertThat(cancaoFormatada, containsString(NOME_ARTISTA));
		assertThat(cancaoFormatada, containsString(TITULO_CANCAO));
	}
}
