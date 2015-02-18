package br.com.colbert.mychart.infraestrutura.io.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da {@link NumeroPosicaoParser}.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 */
public class NumeroPosicaoParserTest extends AbstractTest {

	@Inject
	private NumeroPosicaoParser parser;

	@Test
	public void testParse() throws IOException {
		String linha = "1 A feat. B & C - \"Teste\" [ NEW ] [1ª Semana] [PP:1]";

		Integer posicao = parser.parse(linha);

		assertThat(posicao, is(equalTo(1)));
	}
}
