package br.com.colbert.mychart.aplicacao.cancao;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.*;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.junit.Test;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link CancaoValidador}.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
@AdditionalClasses(CancaoValidador.class)
public class CancaoValidadorTest extends AbstractTest {

	@Inject
	private Validador<Cancao> validador;

	@Test
	public void testValidarCancaoValida() throws ValidacaoException {
		Cancao cancao = new Cancao("Teste", Arrays.asList(Artista.ARTISTA_NULL));
		validador.validar(cancao);
	}

	@Test
	public void testValidarValoresNulos() {
		Cancao cancao = new Cancao(null, (List<Artista>) null);

		try {
			validador.validar(cancao);
			fail("Deveria lançar exceção de validação");
		} catch (ValidacaoException exception) {
			String message = exception.getLocalizedMessage();
			assertThat(message, containsString("título"));
			assertThat(message, containsString("artista"));
		}
	}
}
