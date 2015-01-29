package br.com.colbert.mychart.dominio.topmusical;

import java.time.*;

import br.com.colbert.mychart.infraestrutura.util.EnumUtils;

/**
 * Os diversos períodos de frequência que um top pode ter.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
public enum Frequencia {

	/**
	 * Diário - a cada dia, um novo top é gerado.
	 */
	DIARIO("Diário", Period.ofDays(1)),

	/**
	 * Semanal - a cada semana, um novo top é gerado.
	 */
	SEMANAL("Semanal", Period.ofWeeks(1)),

	/**
	 * Mensal - a cada mês, um novo top é gerado.
	 */
	MENSAL("Mensal", Period.ofMonths(1)),

	/**
	 * Anual - a cada ano, um novo top é gerado
	 */
	ANUAL("Anual", Period.ofYears(1)),

	/**
	 * Decadal - a cada década, um novo top é gerado.
	 */
	DECADAL("Decadal", Period.ofYears(10)),

	/**
	 * Eterno - o top tem vigência permanente.
	 */
	SEMPRE("Eterno", Period.between(LocalDate.MIN, LocalDate.MAX));

	private String descricao;
	private Period periodo;

	private Frequencia(String descricao, Period periodo) {
		this.descricao = descricao;
		this.periodo = periodo;
	}

	public String getDescricao() {
		return descricao;
	}

	/**
	 * Obtém a instância de {@link Period} referente ao período de vigência.
	 * 
	 * @return a instância
	 */
	public Period getPeriodo() {
		return periodo;
	}

	@Override
	public String toString() {
		return EnumUtils.toFormattedString(this);
	}
}
