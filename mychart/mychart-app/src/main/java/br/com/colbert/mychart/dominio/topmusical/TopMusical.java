package br.com.colbert.mychart.dominio.topmusical;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

import org.apache.commons.lang3.builder.ToStringBuilder;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.mychart.dominio.IntervaloDeDatas;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.exception.ElementoJaExistenteException;

/**
 * Uma lista ordenada por classificação das canções dentro de um determinado período de tempo.
 *
 * @author Thiago Colbert
 * @since 07/12/2014
 */
@Entity
@Table(name = "TB_TOP_MUSICAL")
@NamedQueries({ @NamedQuery(name = TopMusical.QUERY_FIND_ATUAL,
		query = "SELECT tm FROM TopMusical tm JOIN FETCH tm.posicoes WHERE tm.periodo.dataInicial = (SELECT MAX(tm1.periodo.dataInicial) FROM TopMusical tm1)") })
public class TopMusical extends AbstractEntidade<Integer> implements Cloneable {

	private static final long serialVersionUID = -2569620277984255920L;

	public static final String QUERY_FIND_ATUAL = "TopMusical.findAtual";

	/**
	 * Instância de um {@link TopMusical} sem nenhuma propriedade definida.
	 */
	public static final TopMusical TOP_MUSICAL_NULL = new TopMusical(null, null, Optional.empty(), Optional.empty());

	public static final int NUMERO_PRIMEIRA_POSICAO = 1;

	@NotNull
	@Min(1)
	@Id
	@Column(name = "NUM_TOP_MUSICAL", unique = true, nullable = false)
	private final Integer numero;

	@NotNull
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "dataInicial", column = @Column(name = "DTA_INICIAL", unique = false, nullable = false)),
			@AttributeOverride(name = "dataFinal", column = @Column(name = "DTA_FINAL", unique = false, nullable = true)) })
	private final IntervaloDeDatas periodo;

	@OneToOne(cascade = {}, fetch = FetchType.LAZY, mappedBy = "proximo", optional = true)
	private final TopMusical anterior;

	@OneToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "NUM_PROXIMO_TOP_MUSICAL", unique = false, nullable = true)
	private final TopMusical proximo;

	@NotNull
	@Valid
	@ElementCollection(fetch = FetchType.LAZY)
	private Map<Integer, Posicao> posicoes = new HashMap<>();

	TopMusical(Integer numero, IntervaloDeDatas periodo, Optional<TopMusical> anterior, Optional<TopMusical> proximo, Map<Integer, Posicao> posicoes) {
		this.numero = numero;
		this.periodo = periodo;
		this.anterior = anterior.orElse(null);
		this.proximo = proximo.orElse(null);
		this.posicoes = new HashMap<>(posicoes);
	}

	TopMusical(Integer numero, IntervaloDeDatas periodo, Optional<TopMusical> anterior, Optional<TopMusical> proximo) {
		this(numero, periodo, anterior, proximo, Collections.emptyMap());
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	public TopMusical() {
		this(null, null, Optional.empty(), Optional.empty());
	}

	/**
	 * Entrada de uma canção dentro do top musical.
	 *
	 * @param numeroPosicao
	 *            número da posição em que a canção entrou
	 * @param cancao
	 *            a canção que estreiou
	 * @return a posição que ocupava anteriormente o número informado
	 * @throws ElementoJaExistenteException
	 *             caso a canção informada já exista dentro do top
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o número da posição seja inválido (< {@value #NUMERO_PRIMEIRA_POSICAO} ou >
	 *             {@link #getQuantidadePosicoes()})
	 */
	public Posicao entrada(Integer numeroPosicao, Cancao cancao) throws ElementoJaExistenteException {
		validarNumeroPosicao(numeroPosicao);
		if (contemCancao(Objects.requireNonNull(cancao, "A canção é obrigatória"))) {
			throw new ElementoJaExistenteException("Canção", cancao);
		}

		return posicoes.put(numeroPosicao, Posicao.nova().noTop(this).comNumero(numeroPosicao).daCancao(cancao));
	}

	/**
	 * Saída de uma canção dentro do top. A tentativa de remover uma posição que já está vazia é uma <code>no-op</code>.
	 *
	 * @param numeroPosicao
	 *            número da posição ocupado pela canção que saiu
	 * @return a posição que ocupava o número informado ou <code>null</code> caso já não estava ocupada
	 * @throws NullPointerException
	 *             caso o número da posição seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o número da posição seja inválido (< {@value #NUMERO_PRIMEIRA_POSICAO} ou >
	 *             {@link #getQuantidadePosicoes()})
	 */
	public Posicao saida(Integer numeroPosicao) {
		validarNumeroPosicao(numeroPosicao);
		return posicoes.remove(numeroPosicao);
	}

	private void validarNumeroPosicao(Integer numeroPosicao) {
		Objects.requireNonNull(numeroPosicao, "O número da posição é obrigatório");
		if (numeroPosicao < NUMERO_PRIMEIRA_POSICAO || numeroPosicao > getQuantidadePosicoes()) {
			throw new IllegalArgumentException("O número da posição deve estar entre " + NUMERO_PRIMEIRA_POSICAO + " e " + getQuantidadePosicoes());
		}
	}

	private boolean contemCancao(Cancao cancao) {
		return posicoes.entrySet().stream().filter(entry -> entry.getValue().getCancao().equals(cancao)).count() > 0;
	}

	@Override
	public Integer getId() {
		return getNumero();
	}

	public Integer getNumero() {
		return numero;
	}

	public IntervaloDeDatas getPeriodo() {
		return periodo;
	}

	public Optional<TopMusical> getAnterior() {
		return Optional.ofNullable(anterior);
	}

	public Optional<TopMusical> getProximo() {
		return Optional.ofNullable(proximo);
	}

	public Map<Integer, Posicao> getPosicoes() {
		return Collections.unmodifiableMap(posicoes);
	}

	public void setPosicoes(Map<Integer, Posicao> posicoes) {
		this.posicoes = new HashMap<>(Objects.requireNonNull(posicoes, "As posições não podem ser nulas"));
	}

	/**
	 * Obtém apenas os objetos {@link Posicao}, ao invés do mapa retornado por {@link #getPosicoes()}.
	 *
	 * @return as posições
	 */
	public List<Posicao> getPosicoesOnly() {
		return getPosicoes().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
	}

	/**
	 * Obtém uma posição do top pelo seu número.
	 *
	 * @param numero
	 *            número da posição desejada
	 * @return a posição
	 * @throws NullPointerException
	 *             caso o número da posição seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o número da posição seja inválido (< {@value #NUMERO_PRIMEIRA_POSICAO} ou >
	 *             {@link #getQuantidadePosicoes()})
	 */
	public Posicao getPosicao(Integer numero) {
		validarNumeroPosicao(numero);
		return posicoes.get(numero);
	}

	/**
	 * Obtém a quantidade de posições no top.
	 *
	 * @return a quantidade de posições
	 */
	public int getQuantidadePosicoes() {
		return posicoes.size();
	}

	@Override
	public Object clone() {
		return new TopMusical(numero, periodo, getAnterior(), getProximo(), posicoes);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("periodo", periodo).append("posicoes", posicoes).toString();
	}
}
