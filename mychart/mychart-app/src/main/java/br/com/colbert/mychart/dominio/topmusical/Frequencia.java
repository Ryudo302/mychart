package br.com.colbert.mychart.dominio.topmusical;

import java.time.*;

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
	DIARIO(Period.ofDays(1)),

	/**
	 * Semanal - a cada semana, um novo top é gerado.
	 */
	SEMANAL(Period.ofWeeks(1)),

	/**
	 * Mensal - a cada mês, um novo top é gerado.
	 */
	MENSAL(Period.ofMonths(1)),

	/**
	 * Anual - a cada ano, um novo top é gerado
	 */
	ANUAL(Period.ofYears(1)),

	/**
	 * Decadal - a cada década, um novo top é gerado.
	 */
	DECADAL(Period.ofYears(10)),

	/**
	 * Eterno - o top tem vigência permanente.
	 */
	SEMPRE(Period.between(LocalDate.MIN, LocalDate.MAX));

	private Period periodo;

	private Frequencia(Period periodo) {
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
