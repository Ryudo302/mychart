package br.com.colbert.mychart.dominio.topmusical;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.time.*;
import java.util.*;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.mychart.dominio.IntervaloDeDatas;
import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link TopMusicalFactory}.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
public class TopMusicalFactoryTest extends AbstractTest {

	private static final int QUANTIDADE_POSICOES = 2;

	@Inject
	private TopMusicalFactory factory;

	@Produces
	public Vigencia vigenciaDosTops() {
		return Vigencia.SEMANAL;
	}

	@Produces
	@QuantidadePosicoes
	public Integer quantidadePosicoes() {
		return QUANTIDADE_POSICOES;
	}

	@Test
	public void testNovo() {
		LocalDate dataInicial = LocalDate.now();
		List<Cancao> cancoes = Arrays.asList(mock(Cancao.class), mock(Cancao.class));

		TopMusical topMusical = factory.novo(dataInicial, cancoes);
		System.out.println("Top criado: " + topMusical);

		assertThat(topMusical.getAnterior().isPresent(), is(false));
		assertThat(topMusical.getProximo().isPresent(), is(false));
		assertThat(topMusical.getNumero(), is(equalTo(1)));
		assertThat(topMusical.getPeriodo(), is(equalTo(IntervaloDeDatas.novo().de(dataInicial).ate(dataInicial.plus(Period.ofWeeks(1))))));

		Map<Integer, Posicao> posicoes = topMusical.getPosicoes();
		assertThat(posicoes.size(), is(equalTo(QUANTIDADE_POSICOES)));

		posicoes.entrySet().forEach(entry -> {
			Posicao posicao = entry.getValue();
			// assertThat(posicao.getNumero(), is(equalTo(...)));
				assertThat(posicao.getMelhorPosicao(), is(equalTo(posicao.getNumero())));
				assertThat(posicao.getPermanenciaMelhorPosicao(), is(equalTo(1)));
				assertThat(posicao.getPermanenciaTotal(), is(equalTo(1)));
				assertThat(posicao.getTopMusical(), is(equalTo(topMusical)));
			});
	}

	@Test
	public void testProximo() {
		Artista artista = new Artista("Fulano", TipoArtista.MASCULINO_SOLO);
		Cancao cancao = new Cancao("Teste", Arrays.asList(artista));

		LocalDate dataInicial = LocalDate.now();
		LocalDate dataFinal = dataInicial.plus(Period.ofWeeks(1));
		TopMusical topMusical = mock(TopMusical.class);

		Posicao posicao1 = Posicao.nova().noTop(topMusical).comNumero(1).daCancao(cancao);
		Posicao posicao2 = Posicao.nova().noTop(topMusical).comNumero(2).daCancao(cancao);

		TopMusical atual = new TopMusical(1, IntervaloDeDatas.novo().de(dataInicial).ate(dataFinal), Optional.empty(), Optional.empty(),
				criarPosicoes(posicao1, posicao2));
		TopMusical proximo = factory.proximo(atual);

		assertThat(proximo.getAnterior().get(), is(equalTo(atual)));
		assertThat(proximo.getNumero(), is(equalTo(2)));
		assertThat(proximo.getPeriodo(),
				is(equalTo(IntervaloDeDatas.novo().de(dataInicial.plus(Period.ofWeeks(1))).ate(dataFinal.plus(Period.ofWeeks(1))))));
		assertThat(proximo.getProximo().isPresent(), is(false));

		Map<Integer, Posicao> posicoesProximo = proximo.getPosicoes();
		assertThat(posicoesProximo.size(), is(equalTo(2)));
		assertThat(posicoesProximo.get(1), is(equalTo(posicao1.permanencia())));
		assertThat(posicoesProximo.get(2), is(equalTo(posicao2.permanencia())));
	}

	private Map<Integer, Posicao> criarPosicoes(Posicao posicao1, Posicao posicao2) {
		Map<Integer, Posicao> posicoes = new HashMap<>(2);
		posicoes.put(1, posicao1);
		posicoes.put(2, posicao2);
		return posicoes;
	}
}
