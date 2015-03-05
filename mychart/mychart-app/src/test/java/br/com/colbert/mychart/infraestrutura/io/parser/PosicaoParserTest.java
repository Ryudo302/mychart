package br.com.colbert.mychart.infraestrutura.io.parser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import br.com.colbert.mychart.dominio.topmusical.Posicao;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link PosicaoParser}.
 *
 * @author Thiago Colbert
 * @since 17/02/2015
 */
@Ignore
public class PosicaoParserTest extends AbstractTest {

	@Inject
	private PosicaoParser parser;

	@Test
	public void testParse() throws URISyntaxException {
		Path arquivo = Paths.get(FileUtils.toFile(Thread.currentThread().getContextClassLoader().getResource("tops")).getAbsolutePath(), "A.txt");

		List<Posicao> posicoes = parser.parse(arquivo);

		assertThat(posicoes, is(notNullValue(List.class)));
		assertThat(posicoes.size(), is(equalTo(5)));
	}
}
