package br.com.colbert.mychart.infraestrutura.exception;

/**
 * Exceção lançada quando ocorre algum erro durante a execução de alguma operação dos repositórios.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class RepositoryException extends Exception {

	private static final long serialVersionUID = 2878847274703608631L;

	public RepositoryException() {
		super();
	}

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryException(String message) {
		super(message);
	}
}
