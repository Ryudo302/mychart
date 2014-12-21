package br.com.colbert.mychart.infraestrutura.eventos.artista;

/**
 * Enumeração dos modos de consulta possíveis.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
public enum ModoConsulta {

	/**
	 * Consulta somente elementos que já estão incluídos em um repositório local.
	 */
	SOMENTE_JA_INCLUIDOS,

	/**
	 * Consulta por todos os elementos, independentemente de estarem presentes ou não em um repositório local.
	 */
	TODOS;
}
