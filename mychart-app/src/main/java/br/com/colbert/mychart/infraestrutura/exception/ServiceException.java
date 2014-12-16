package br.com.colbert.mychart.infraestrutura.exception;

/**
 * Exceção lançada quando ocorre um erro em algum dos serviços.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 231624586594038180L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}
