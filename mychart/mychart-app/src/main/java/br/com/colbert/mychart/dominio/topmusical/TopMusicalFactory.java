package br.com.colbert.mychart.dominio.topmusical;

import java.time.LocalDate;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.IntervaloDeDatas;
import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Fábrica de instâncias de {@link TopMusical}.
 *
 * @author Thiago Colbert
 * @since 07/12/2014
 */
public class TopMusicalFactory {

	@Inject
	private transient Logger logger;

	private final Vigencia vigenciaDosTops;
	private final Integer quantidadePosicoes;

	/**
	 * Cria uma fábrica definindo uma {@link Vigencia} para as instâncias de {@link TopMusical} que serão criadas por ela.
	 * 
	 * @param vigenciaDosTops
	 *            a ser utilizada na criação das instâncias de top musical
	 * @param quantidadePosicoes
	 *            a quantidade de posições dos tops gerados
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 */
	@Inject
	public TopMusicalFactory(Vigencia vigenciaDosTops, @QuantidadePosicoes Integer quantidadePosicoes) {
		this.quantidadePosicoes = Objects.requireNonNull(quantidadePosicoes, "A quantidade de posições é obrigatória");
		this.vigenciaDosTops = Objects.requireNonNull(vigenciaDosTops, "A vigência obrigatória");
	}

	@PostConstruct
	protected void logInfo() {
		logger.info("Vigência dos tops: {}", vigenciaDosTops);
		logger.info("Quantidade de posições: {}", quantidadePosicoes);
	}

	/**
	 * Cria um novo {@link TopMusical} com valores iniciais padrão. Normalmente este método deve ser utilizado quando não existe outro top a
	 * partir do qual possa ser criado um novo - método {@link #proximo(TopMusical)}.
	 * 
	 * @param dataInicial
	 *            a data inicial do período de vigência do top criado
	 * @param cancoes
	 *            as canções a serem utilizadas nas posições do top
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso não seja informado um número de canções exatamente igual a {@link #getQuantidadePosicoes()}
	 * @return o top criado
	 */
	public TopMusical novo(LocalDate dataInicial, List<Cancao> cancoes) {
		Validate.notNull(dataInicial, "A data inicial é obrigatória");
		Validate.notNull(cancoes, "As canções são obrigatórias");
		Validate.isTrue(cancoes.size() == quantidadePosicoes, "Devem ser informadas " + quantidadePosicoes + " canções");

		TopMusical topMusical = new TopMusical(1, IntervaloDeDatas.novo().de(dataInicial)
				.ate(dataInicial.plus(vigenciaDosTops.getPeriodo())), Optional.empty(), Optional.empty());
		logger.debug("Criando top vazio: {}", topMusical);

		Map<Integer, Posicao> posicoes = new HashMap<>(quantidadePosicoes);
		int numero = 1;
		for (Cancao cancao : cancoes) {
			posicoes.put(numero, Posicao.nova().noTop(topMusical).comNumero(numero).daCancao(cancao));
			numero++;
		}

		logger.debug("Definindo posições do top: {}", posicoes);
		topMusical.setPosicoes(posicoes);

		return topMusical;
	}

	/**
	 * Cria um novo {@link TopMusical} a partir de outro. O top gerado corresponderá ao top seguinte a partir do top informado.
	 *
	 * @param atual
	 *            o top a ser utilizado como base
	 * @return o top seguinte
	 * @throws NullPointerException
	 *             caso o top informado seja <code>null</code>
	 */
	public TopMusical proximo(TopMusical atual) {
		Validate.notNull(atual, "Top musical atual nulo");
		logger.debug("Gerando próximo top a partir de: {}", atual);

		Map<Integer, Posicao> posicoesAtual = atual.getPosicoes();
		Map<Integer, Posicao> posicoesProximo = new HashMap<>(posicoesAtual.size());

		posicoesAtual.forEach((numero, posicao) -> posicoesProximo.put(numero, posicao.permanencia()));
		logger.debug("Posições do próximo top: {}", posicoesProximo);

		return new TopMusical(atual.getNumero() + 1, atual.getPeriodo().adicionar(vigenciaDosTops.getPeriodo()), Optional.of(atual),
				Optional.empty(), posicoesProximo);
	}

	public Integer getQuantidadePosicoes() {
		return quantidadePosicoes;
	}
}
