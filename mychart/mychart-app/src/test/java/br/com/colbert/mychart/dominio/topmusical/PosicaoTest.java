package br.com.colbert.mychart.dominio.topmusical;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.inject.Inject;
import javax.validation.*;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link Posicao}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class PosicaoTest extends AbstractTest {

	private static final Integer INTEGER_TWO = Integer.valueOf(2);

	@Mock
	private TopMusical topMusical;

	@Inject
	private Validator validator;

	@Test
	public void testValidacao() {
		Posicao posicao = Posicao.nova().noTop(topMusical).comNumero(999).daCancao(new Cancao("teste", Collections.emptyList()));
		Set<ConstraintViolation<Posicao>> violacoes = validator.validate(posicao);
		violacoes.forEach(System.out::println);
	}

	@Test
	public void testNova() {
		Posicao posicao = criarPosicao();

		assertThat(posicao.getMelhorPosicao(), is(equalTo(NumberUtils.INTEGER_ONE)));
		assertThat(posicao.getPermanenciaMelhorPosicao(), is(equalTo(NumberUtils.INTEGER_ONE)));
		assertThat(posicao.getPermanenciaTotal(), is(equalTo(NumberUtils.INTEGER_ONE)));
	}

	@Test
	public void testPermanencia() {
		Posicao posicao = criarPosicao();
		Posicao permanencia = posicao.permanencia();

		assertThat(permanencia.getNumero(), is(equalTo(posicao.getNumero())));
		assertThat(permanencia.getMelhorPosicao(), is(equalTo(NumberUtils.INTEGER_ONE)));
		assertThat(permanencia.getPermanenciaMelhorPosicao(), is(equalTo(INTEGER_TWO)));
		assertThat(permanencia.getPermanenciaTotal(), is(equalTo(INTEGER_TWO)));
	}

	private Posicao criarPosicao() {
		return Posicao.nova().noTop(topMusical).comNumero(NumberUtils.INTEGER_ONE)
				.daCancao(new Cancao("Teste", Arrays.asList(new Artista("Fulano", TipoArtista.MASCULINO_SOLO))));
	}
}
