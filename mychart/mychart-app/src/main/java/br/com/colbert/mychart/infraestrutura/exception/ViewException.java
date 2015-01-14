package br.com.colbert.mychart.infraestrutura.exception;

/**
 * Exceção lançada quando ocorre algum erro relacionado à camada de visão.
 * 
 * @author Thiago Colbert
 * @since 14/01/2015
 */
public class ViewException extends RuntimeException {

	private static final long serialVersionUID = -868427663019844208L;

	public ViewException() {
		super();
	}

	public ViewException(String message, Throwable cause) {
		super(message, cause);
	}

	public ViewException(String message) {
		super(message);
	}

	public ViewException(Throwable cause) {
		super(cause);
	}
}
