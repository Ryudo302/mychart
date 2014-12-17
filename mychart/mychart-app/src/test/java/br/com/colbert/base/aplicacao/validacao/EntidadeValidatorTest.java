package br.com.colbert.base.aplicacao.validacao;

import javax.inject.Inject;
import javax.validation.constraints.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link EntidadeValidator}.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
public class EntidadeValidatorTest extends AbstractTest {

	/**
	 * Classe utilizada nos testes.
	 * 
	 * @author Thiago Colbert
	 * @since 13/12/2014
	 */
	public class Exemplo extends AbstractEntidade<Integer> {

		private static final long serialVersionUID = -2372766954568615017L;

		private Integer id;

		@NotNull
		@Size(min = 1, max = 255)
		private String nome;

		public Exemplo(String nome) {
			this.nome = nome;
		}

		@Override
		public Integer getId() {
			return id;
		}
	}

	@Inject
	private EntidadeValidator validador;

	@Test(expected = ValidacaoException.class)
	public void testValidarValoresNulos() throws ValidacaoException {
		Exemplo exemplo = new Exemplo(null);
		validador.validar(exemplo);
	}

	@Test(expected = ValidacaoException.class)
	public void testValidarNomeMuitoPequeno() throws ValidacaoException {
		Exemplo exemplo = new Exemplo(StringUtils.EMPTY);
		validador.validar(exemplo);
	}

	@Test(expected = ValidacaoException.class)
	public void testValidarNomeMuitoGrande() throws ValidacaoException {
		Exemplo exemplo = new Exemplo(StringUtils.repeat('x', 256));
		validador.validar(exemplo);
	}

	@Test()
	public void testValidarExemploValido() throws ValidacaoException {
		Exemplo exemplo = new Exemplo("Teste");
		validador.validar(exemplo);
	}
}
