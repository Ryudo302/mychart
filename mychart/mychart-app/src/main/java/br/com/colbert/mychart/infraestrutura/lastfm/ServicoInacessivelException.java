package br.com.colbert.mychart.infraestrutura.lastfm;

import br.com.colbert.mychart.infraestrutura.exception.ServiceException;

/**
 * Exceção lançada quando um serviço se encontra inacessível ou indisponível.
 * 
 * @author Thiago Colbert
 * @since 26/12/2014
 */
public class ServicoInacessivelException extends ServiceException {

	private static final long serialVersionUID = -329025860951005739L;

	private static final String MENSAGEM_PADRAO = "O serviço não está acessível ou disponível no momento.";

	public ServicoInacessivelException() {
		super(MENSAGEM_PADRAO);
	}

	public ServicoInacessivelException(Throwable cause) {
		super(MENSAGEM_PADRAO, cause);
	}
}
