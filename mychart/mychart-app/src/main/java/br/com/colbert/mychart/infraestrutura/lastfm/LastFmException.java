package br.com.colbert.mychart.infraestrutura.lastfm;

import de.umass.lastfm.Result;

import br.com.colbert.mychart.infraestrutura.exception.ServiceException;

/**
 * Exceção lançada quando ocorre algum problema na comunicação com os WebServices da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public class LastFmException extends ServiceException {

	private static final long serialVersionUID = 7797201400272665724L;

	private static final String MESSAGE_SEPARATOR = " | ";

	/**
	 * Cria uma nova exceção a partir das informações contidas em um {@link Result}.
	 * 
	 * @param result
	 *            o resultado de uma chamada a um WebService da LastFM
	 * @throws IllegalArgumentException
	 *             caso o resultado informado não seja de erro
	 */
	public LastFmException(Result result) {
		super(criarMensagem(result));
	}

	/**
	 * Cria uma nova exceção com a mensagem de erro e a exceção-causa informadas.
	 * 
	 * @param message
	 *            a mensagem de erro
	 * @param cause
	 *            a causa desta exceção
	 */
	public LastFmException(String message, Throwable cause) {
		super(message, cause);
	}

	private static String criarMensagem(Result result) {
		if (result.isSuccessful()) {
			throw new IllegalArgumentException("Não é um resultado de erro: " + result);
		}

		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("código HTTP: ").append(result.getHttpErrorCode()).append(MESSAGE_SEPARATOR)
				.append("código LastFM: ").append(result.getErrorCode()).append(MESSAGE_SEPARATOR).append("mensagem: ")
				.append(result.getErrorMessage());
		return messageBuilder.toString();
	}
}
