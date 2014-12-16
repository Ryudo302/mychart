package br.com.colbert.mychart.dominio;

import java.io.Serializable;
import java.time.*;
import java.util.Objects;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.*;

/**
 * Um intervalo de tempo representado por duas datas - uma inicial e uma final. Esta classe é imutável.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@Embeddable
public final class IntervaloDeDatas implements Comparable<IntervaloDeDatas>, Serializable {

	private static final long serialVersionUID = 9056254908844013277L;

	private final LocalDate dataInicial;
	private final LocalDate dataFinal;

	private IntervaloDeDatas(LocalDate dataInicial, LocalDate dataFinal) {
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}

	/**
	 * Cria um novo intervalo de datas.
	 *
	 * @return objeto que precisa ter definida a data inicial do intervalo
	 */
	public static SemDataInicial novo() {
		return new SemDataInicial();
	}

	/**
	 * Obtém um novo {@link IntervaloDeDatas} a partir da adição do período de tempo informado no intervalo atual.
	 *
	 * @param periodo
	 *            de tempo a ser adicionado
	 * @return o novo intervalo de tempo
	 * @throws NullPointerException
	 *             caso o período seja <code>null</code>
	 */
	public IntervaloDeDatas adicionar(Period periodo) {
		Objects.requireNonNull(periodo, "O período a ser adicionado é obrigatório");
		return new IntervaloDeDatas(dataInicial.plus(periodo), dataFinal.plus(periodo));
	}

	public LocalDate getDataInicial() {
		return dataInicial;
	}

	public LocalDate getDataFinal() {
		return dataFinal;
	}

	@Override
	public int compareTo(IntervaloDeDatas other) {
		return CompareToBuilder.reflectionCompare(this, other);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de um {@link IntervaloDeDatas}. Esta necessita que seja
	 * definida a data inicial do intervalo.
	 *
	 * @author Thiago Colbert
	 * @since 09/12/2014
	 */
	public static class SemDataInicial implements Serializable {

		private static final long serialVersionUID = -3808985350543103171L;

		/**
		 * Define a data inicial do intervalo.
		 *
		 * @param dataInicial
		 *            a data inicial
		 * @return objeto que precisa ter definida a data final do intervalo
		 */
		public SemDataFinal de(LocalDate dataInicial) {
			return new SemDataFinal(dataInicial);
		}
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de um {@link IntervaloDeDatas}. Esta necessita que seja
	 * definida a data final do intervalo.
	 *
	 * @author Thiago Colbert
	 * @since 09/12/2014
	 */
	public static class SemDataFinal implements Serializable {

		private static final long serialVersionUID = -1665431783347662584L;

		private final LocalDate dataInicial;

		private SemDataFinal(LocalDate dataInicial) {
			this.dataInicial = dataInicial;
		}

		/**
		 * Define a data final do intervalo.
		 *
		 * @param dataFinal
		 *            a data final
		 * @return o intervalo criado
		 * @throws IllegalArgumentException
		 *             caso a data final seja inferior à data inicial
		 */
		public IntervaloDeDatas ate(LocalDate dataFinal) {
			if (dataFinal.isBefore(dataInicial)) {
				throw new IllegalArgumentException("A data final deve ser superior à data inicial");
			}

			return new IntervaloDeDatas(dataInicial, dataFinal);
		}
	}
}