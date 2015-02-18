package br.com.colbert.mychart.infraestrutura.io.parser;

/**
 * Exceção lançada quando ocorre algum erro durante a leitura de um arquivo para extrair algum dado.
 * 
 * @author Thiago Colbert
 * @since 17/02/2015
 */
public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 110953078967716999L;

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}
}
