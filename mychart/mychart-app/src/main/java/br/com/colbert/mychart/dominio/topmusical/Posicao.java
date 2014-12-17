package br.com.colbert.mychart.dominio.topmusical;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

import org.apache.commons.lang3.builder.*;
import org.apache.commons.lang3.math.NumberUtils;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.topmusical.validator.NumeroPosicao;

/**
 * Uma posição dentro de um top musical, associando uma canção à um número de posição.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
@Entity
@Table(name = "TB_POSICAO")
public class Posicao extends AbstractEntidade<PosicaoId> {

	private static final long serialVersionUID = -9019104768183598309L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "numeroTopMusical", column = @Column(name = "NUM_TOP_MUSICAL", unique = false, nullable = false, insertable = true, updatable = false)),
			@AttributeOverride(name = "idCancao", column = @Column(name = "COD_CANCAO", unique = false, nullable = false, insertable = true, updatable = false)) })
	private PosicaoId id;

	@NotNull
	@NumeroPosicao
	@Column(name = "NUM_POSICAO", unique = false, nullable = false)
	private Integer numero;

	@NotNull
	@Valid
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "NUM_TOP_MUSICAL", unique = false, nullable = false, insertable = false, updatable = false)
	private TopMusical topMusical;

	@NotNull
	@Valid
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "COD_CANCAO", unique = false, nullable = false, insertable = false, updatable = false)
	private Cancao cancao;

	@NotNull
	@NumeroPosicao
	@Column(name = "NUM_MELHOR_POSICAO", unique = false, nullable = false)
	private Integer melhorPosicao;

	@NotNull
	@Min(1)
	@Column(name = "NUM_PERMANENCIA_MELHOR_POSICAO", unique = false, nullable = false)
	private Integer permanenciaMelhorPosicao;

	@NotNull
	@Min(1)
	@Column(name = "NUM_PERMANENCIA_TOTAL", unique = false, nullable = false)
	private Integer permanenciaTotal;

	private Posicao(Integer numero, TopMusical topMusical, Cancao cancao, Integer melhorPosicao,
			Integer permanenciaMelhorPosicao, Integer permanenciaTotal) {
		this.numero = numero;
		this.topMusical = topMusical;
		this.cancao = cancao;
		this.melhorPosicao = melhorPosicao;
		this.permanenciaMelhorPosicao = permanenciaMelhorPosicao;
		this.permanenciaTotal = permanenciaTotal;
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	Posicao() {
		this(null, null, null, null, null, null);
	}

	/**
	 * Cria uma nova posição. Todas as informações de histórico estarão com valor inicial.
	 * 
	 * @return objeto que necessita ter o top musical definido
	 */
	public static SemTopMusical nova() {
		return new SemTopMusical();
	}

	/**
	 * Obtém uma nova {@link Posicao} referente a nenhuma alteração da posição atual no top seguinte.
	 * 
	 * @return a instância criada
	 */
	public Posicao permanencia() {
		return new Posicao(numero, topMusical, cancao, melhorPosicao, numero == melhorPosicao ? permanenciaMelhorPosicao + 1
				: permanenciaMelhorPosicao, permanenciaTotal + 1);
	}

	@Override
	public PosicaoId getId() {
		return id;
	}

	public Integer getNumero() {
		return numero;
	}

	public TopMusical getTopMusical() {
		return topMusical;
	}

	public Cancao getCancao() {
		return cancao;
	}

	public Integer getMelhorPosicao() {
		return melhorPosicao;
	}

	public Integer getPermanenciaMelhorPosicao() {
		return permanenciaMelhorPosicao;
	}

	public Integer getPermanenciaTotal() {
		return permanenciaTotal;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("cancao", cancao)
				.toString();
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de uma {@link Posicao}. Esta necessita que seja
	 * definido o top musical.
	 * 
	 * @author Thiago Colbert
	 * @since 09/12/2014
	 */
	public static class SemTopMusical implements Serializable {

		private static final long serialVersionUID = 8317509475182948587L;

		/**
		 * Define o top musical.
		 * 
		 * @param topMusical
		 *            o top musical
		 * @return objeto que necessita ter definido o número da posição
		 * @throws NullPointerException
		 *             caso o top informado seja <code>null</code>
		 */
		public SemNumero noTop(TopMusical topMusical) {
			return new SemNumero(Objects.requireNonNull(topMusical, "O top musical é obrigatório"));
		}
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de uma {@link Posicao}. Esta necessita que seja
	 * definido o número da posição.
	 * 
	 * @author Thiago Colbert
	 * @since 09/12/2014
	 */
	public static class SemNumero implements Serializable {

		private static final long serialVersionUID = 6723522766030614855L;

		private final TopMusical topMusical;

		private SemNumero(TopMusical topMusical) {
			this.topMusical = topMusical;
		}

		/**
		 * Define o número da posição.
		 * 
		 * @param numero
		 *            o número da posição
		 * @return objeto que necessita ter a canção definida
		 * @throws NullPointerException
		 *             caso o número seja <code>null</code>
		 */
		public SemCancao comNumero(Integer numero) {
			return new SemCancao(topMusical, Objects.requireNonNull(numero, "O número é obrigatório"));
		}
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de uma {@link Posicao}. Esta necessita que seja
	 * definida a canção.
	 * 
	 * @author Thiago Colbert
	 * @since 09/12/2014
	 */
	public static class SemCancao implements Serializable {

		private static final long serialVersionUID = -715721416939900923L;

		private TopMusical topMusical;
		private Integer numero;

		private SemCancao(TopMusical topMusical, Integer numero) {
			this.topMusical = topMusical;
			this.numero = numero;
		}

		/**
		 * Define a canção que está na posição.
		 * 
		 * @param cancao
		 *            a canção
		 * @return a instância de posição criada
		 * @throws NullPointerException
		 *             caso a canção seja <code>null</code>
		 */
		public Posicao daCancao(Cancao cancao) {
			return new Posicao(numero, topMusical, Objects.requireNonNull(cancao, "A canção é obrigatória"), numero,
					NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE);
		}
	}
}
