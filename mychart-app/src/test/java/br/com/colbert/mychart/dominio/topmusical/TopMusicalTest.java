package br.com.colbert.mychart.dominio.topmusical;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.colbert.mychart.dominio.IntervaloDeDatas;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.exception.ElementoJaExistenteException;

/**
 * Testes unitários da classe {@link TopMusical}.
 * 
 * @author Thiago Colbert
 * @since 10/12/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class TopMusicalTest {

	private TopMusical top;

	@Mock
	private Posicao posicao1;
	@Mock
	private Posicao posicao2;

	@Mock
	private Cancao cancao1;
	@Mock
	private Cancao cancao2;

	@Before
	public void setUp() {
		top = new TopMusical(1, IntervaloDeDatas.novo().de(LocalDate.now()).ate(LocalDate.now()), Optional.empty(), Optional.empty(),
				criarPosicoes(posicao1, posicao2));

		when(posicao1.getCancao()).thenReturn(cancao1);
		when(posicao2.getCancao()).thenReturn(cancao2);

		when(posicao1.getNumero()).thenReturn(1);
		when(posicao2.getNumero()).thenReturn(2);
	}

	private Map<Integer, Posicao> criarPosicoes(Posicao posicao1, Posicao posicao2) {
		Map<Integer, Posicao> posicoes = new HashMap<>(2);
		posicoes.put(1, posicao1);
		posicoes.put(2, posicao2);
		return posicoes;
	}

	@Test
	public void testEntrada() throws ElementoJaExistenteException {
		Cancao cancao = mock(Cancao.class);

		Posicao posicaoAnterior = top.entrada(1, cancao);

		assertThat(posicaoAnterior.getNumero(), is(equalTo(1)));
		assertThat(posicaoAnterior.getCancao(), is(equalTo(posicao1.getCancao())));
	}

	@Test(expected = ElementoJaExistenteException.class)
	public void deveriaLancaoExceptionQuandoEntraCancaoQueJaExisteNoTop() throws ElementoJaExistenteException {
		top.entrada(1, cancao1);
	}

	@Test
	public void testSaida() {
		Posicao posicao = top.saida(1);

		assertThat(posicao.getNumero(), is(equalTo(1)));
		assertThat(posicao.getCancao(), is(equalTo(posicao1.getCancao())));
		assertThat(top.getPosicao(1), is(nullValue()));
	}

	@Test
	public void deveriaRetornarNullQuandoTentarRemoverPosicaoJaVazia() {
		top.saida(1);

		Posicao posicao = top.saida(1);

		assertThat(posicao, is(nullValue()));
	}
}
