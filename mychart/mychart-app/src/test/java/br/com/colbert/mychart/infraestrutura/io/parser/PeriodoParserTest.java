package br.com.colbert.mychart.infraestrutura.io.parser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.LocalDate;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import br.com.colbert.mychart.dominio.IntervaloDeDatas;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link PeriodoParser}.
 * 
 * @author Thiago Colbert
 * @since 17/02/2015
 */
public class PeriodoParserTest extends AbstractTest {

	@Inject
	private PeriodoParser parser;

	@Test
	public void testParse() throws URISyntaxException {
		LocalDate dataInicial = LocalDate.of(2015, 2, 8);
		LocalDate dataFinal = LocalDate.of(2015, 2, 14);

		IntervaloDeDatas intervaloEsperado = IntervaloDeDatas.novo().de(dataInicial).ate(dataFinal);

		Path arquivo = Paths.get(FileUtils.toFile(Thread.currentThread().getContextClassLoader().getResource("tops"))
				.getAbsolutePath(), "A.txt");

		IntervaloDeDatas intervaloDatas = parser.parse(arquivo);

		assertThat(intervaloDatas, is(notNullValue(IntervaloDeDatas.class)));
		assertThat(intervaloDatas, is(equalTo(intervaloEsperado)));
	}
}
