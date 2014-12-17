package br.com.colbert.mychart.dominio.topmusical;

import java.time.*;

/**
 * Os diversos períodos de vigência que um top pode ter.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
public enum Vigencia {

	/**
	 * Período diário - a cada dia, um novo top é gerado.
	 */
	DIARIO(Period.ofDays(1)),

	/**
	 * Período semanal - a cada semana, um novo top é gerado.
	 */
	SEMANAL(Period.ofWeeks(1)),

	/**
	 * Período mensal - a cada mês, um novo top é gerado.
	 */
	MENSAL(Period.ofMonths(1)),

	/**
	 * Período anual - a cada ano, um novo top é gerado
	 */
	ANUAL(Period.ofYears(1)),

	/**
	 * Período decadal - a cada década, um novo top é gerado.
	 */
	DECADAL(Period.ofYears(10)),

	/**
	 * Período eterno - o top tem vigência permanente.
	 */
	SEMPRE(Period.between(LocalDate.MIN, LocalDate.MAX));

	private Period periodo;

	private Vigencia(Period periodo) {
		this.periodo = periodo;
	}

	/**
	 * Obtém a instância de {@link Period} referente ao período de vigência.
	 * 
	 * @return a instância
	 */
	public Period getPeriodo() {
		return periodo;
	}
}
