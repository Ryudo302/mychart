package br.com.colbert.mychart.aplicacao.artista;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jglue.cdiunit.AdditionalClasses;
import org.junit.Test;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link ArtistaValidador}.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
@AdditionalClasses(ArtistaValidador.class)
public class ArtistaValidadorTest extends AbstractTest {

	private static final int TAMANHO_MAXIMO_NOME = 255;

	@Inject
	private Validador<Artista> validador;

	@Test
	public void testValidarArtistaValido() throws ValidacaoException {
		Artista artista = new Artista("Fulano", TipoArtista.MASCULINO_SOLO);
		validador.validar(artista);
	}

	@Test
	public void testValidarValoresNulos() {
		Artista artista = new Artista(null, null);

		try {
			validador.validar(artista);
			fail("Deveria lançar exceção de validação");
		} catch (ValidacaoException exception) {
			assertThat(exception.getLocalizedMessage(), containsString("nome"));
			assertThat(exception.getLocalizedMessage(), containsString("tipo"));
		}
	}

	@Test
	public void testValidarNomeVazio() {
		Artista artista = new Artista(StringUtils.EMPTY, TipoArtista.DESCONHECIDO);

		try {
			validador.validar(artista);
			fail("Deveria lançar exceção de validação");
		} catch (ValidacaoException exception) {
			assertThat(exception.getLocalizedMessage(), containsString("nome"));
		}
	}

	@Test
	public void testValidarNomeMuitoGrande() {
		Artista artista = new Artista(StringUtils.repeat('x', TAMANHO_MAXIMO_NOME + 1), TipoArtista.DESCONHECIDO);

		try {
			validador.validar(artista);
			fail("Deveria lançar exceção de validação");
		} catch (ValidacaoException exception) {
			assertThat(exception.getLocalizedMessage(), containsString("nome"));
		}
	}
}
