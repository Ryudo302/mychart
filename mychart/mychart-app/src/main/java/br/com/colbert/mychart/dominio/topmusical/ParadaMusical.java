package br.com.colbert.mychart.dominio.topmusical;

import javax.enterprise.inject.Produces;

/**
 * Classe que centraliza todas as principais configurações da parada musical.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ParadaMusical {

	private Vigencia vigenciaDosTops;
	private Integer quantidadePosicoesDosTops;

	/**
	 * Obtém a vigência dos tops principais.
	 * 
	 * @return a vigência
	 */
	@Produces
	public Vigencia getVigenciaDosTops() {
		return vigenciaDosTops;
	}

	@Produces
	@QuantidadePosicoes
	public Integer getQuantidadePosicoesDosTops() {
		return quantidadePosicoesDosTops;
	}
}
