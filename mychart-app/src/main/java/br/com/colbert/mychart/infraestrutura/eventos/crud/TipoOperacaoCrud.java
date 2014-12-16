package br.com.colbert.mychart.infraestrutura.eventos.crud;

/**
 * As operações CRUD.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public enum TipoOperacaoCrud {

	/**
	 * A operação de inserção de novos elementos.
	 */
	INSERCAO,
	
	/**
	 * A operação de atualização de elementos pré-existentes.
	 */
	ATUALIZACAO,
	
	/**
	 * A operação de remoção de elementos pré-existentes.
	 */
	REMOCAO,
	
	/**
	 * A operação de consulta de elementos existentes.
	 */
	CONSULTA;
}
