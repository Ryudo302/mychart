package br.com.colbert.mychart.infraestrutura.eventos.topmusical;

/**
 * Os tipos de mudanças possíveis de ocorrer dentro de um top musical.
 * 
 * @author Thiago Colbert
 * @since 09/12/2014
 */
public enum TipoMudancaTopMusical {

	/**
	 * Uma canção subiu dentro do top para uma posição menor.
	 */
	SUBIDA,

	/**
	 * Uma canção caiu dentro do top para uma posição maior.
	 */
	DESCIDA,

	/**
	 * Uma canção entrou no top pela primeira vez.
	 */
	ESTREIA,

	/**
	 * Uma canção que já esteve no top anteriormente retornou a ele.
	 */
	RETORNO,

	/**
	 * Uma canção saiu do top.
	 */
	SAIDA;
}
