package br.com.colbert.mychart.infraestrutura.io;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;

import javax.inject.Inject;

import org.junit.*;

import br.com.colbert.mychart.dominio.importing.ParadaMusical;
import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link InformacoesParadaPathImporter}.
 *
 * @author Thiago Colbert
 * @since 17/02/2015
 */
@Ignore
public class InformacoesParadaPathImporterTest extends AbstractTest {

	@Inject
	private InformacoesParadaPathImporter importer;

	@Test
	public void testImportar() throws URISyntaxException {
		Path fonte = Paths.get(Thread.currentThread().getContextClassLoader().getResource("tops").toURI());
		ParadaMusical paradaMusical = importer.importar(fonte);

		assertThat(paradaMusical, is(notNullValue(ParadaMusical.class)));

		List<TopMusical> tops = paradaMusical.getTops();
		assertThat(tops, is(notNullValue(List.class)));
		assertThat(tops.size(), is(equalTo(2)));

		tops.forEach(top -> {
			assertThat(top, is(notNullValue(TopMusical.class)));
		});

		TopMusical topA = tops.get(0);
		TopMusical topB = tops.get(1);

		assertThat(topA.getAnterior().isPresent(), is(false));
		assertThat(topA.getProximo().get(), is(equalTo(topB)));

		assertThat(topB.getAnterior().get(), is(equalTo(topA)));
		assertThat(topB.getProximo().isPresent(), is(false));
	}
}
