package br.com.colbert.mychart.infraestrutura.io.parser;

/**
 * Permite o processamento de um determinado tipo de objeto para gerar um de outro tipo.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 *
 * @param <F>
 *            tipo da fonte
 * @param <T>
 *            tipo do objeto gerado
 */
public interface Parser<F, T> {

	/**
	 * Processa um elemento.
	 * 
	 * @param fonte
	 *            a ser processada
	 * @return os dados extraídos do arquivo
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma fonte inválida
	 * @throws ParserException
	 *             caso ocorra algum erro durante o processamento da fonte
	 */
	T parse(F fonte);
}
