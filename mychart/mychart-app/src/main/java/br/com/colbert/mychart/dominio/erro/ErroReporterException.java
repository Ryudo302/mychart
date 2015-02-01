package br.com.colbert.mychart.dominio.erro;

/**
 * Exceção lançada quando ocorre algum problema durante o envio de uma notificação de erro.
 * 
 * @author Thiago Colbert
 * @since 01/02/2015
 */
public class ErroReporterException extends RuntimeException {

	private static final long serialVersionUID = -6193996625356341739L;

	public ErroReporterException() {
		super();
	}

	public ErroReporterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErroReporterException(String message) {
		super(message);
	}

	public ErroReporterException(Throwable cause) {
		super(cause);
	}
}
