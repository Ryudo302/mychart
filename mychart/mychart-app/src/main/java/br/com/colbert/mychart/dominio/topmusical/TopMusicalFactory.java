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

	private final TopMusicalConfiguration config;

	/**
	 * Cria uma fábrica definindo uma {@link Frequencia} para as instâncias de {@link TopMusical} que serão criadas por ela.
	 * 
	 * @param config
	 *            configurações dos tops musicais criados
	 * @throws NullPointerException
	 *             caso o parâmetro seja <code>null</code>
	 */
	@Inject
	public TopMusicalFactory(TopMusicalConfiguration config) {
		this.config = Objects.requireNonNull(config, "A configuração dos tops musicais é obrigatória!");
	}

	@PostConstruct
	protected void logInfo() {
		logger.info("Configuração dos tops: {}", config);
	}

	/**
	 * Cria um novo {@link TopMusical} com valores iniciais padrão. Normalmente este método deve ser utilizado quando não existe
	 * outro top a partir do qual possa ser criado um novo - método {@link #proximo(TopMusical)}.
	 * 
	 * @param dataInicial
	 *            a data inicial do período de vigência do top criado
	 * @param cancoes
	 *            as canções a serem utilizadas nas posições do top
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso não seja informado um número de canções exatamente igual ao definido por {@link #getConfig()}
	 * @return o top criado
	 */
	public TopMusical novo(LocalDate dataInicial, List<Cancao> cancoes) {
		Validate.notNull(dataInicial, "A data inicial é obrigatória");
		Validate.notNull(cancoes, "As canções são obrigatórias");

		Integer quantidadePosicoes = config.getQuantidadePosicoes();
		Validate.isTrue(cancoes.size() == quantidadePosicoes, "Devem ser informadas " + quantidadePosicoes + " canções");

		TopMusical topMusical = new TopMusical(1, IntervaloDeDatas.novo().de(dataInicial)
				.ate(dataInicial.plus(config.getFrequencia().getPeriodo())), Optional.empty(), Optional.empty());
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

		return new TopMusical(atual.getNumero() + 1, atual.getPeriodo().adicionar(config.getFrequencia().getPeriodo()),
				Optional.of(atual), Optional.empty(), posicoesProximo);
	}

	/**
	 * Obtém as configurações dos tops musicais criados por esta fábrica.
	 * 
	 * @return as configurações dos tops musicais criados
	 */
	public TopMusicalConfiguration getConfig() {
		return config;
	}
}
