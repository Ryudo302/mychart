package br.com.colbert.mychart.infraestrutura.lastfm.api;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Exceção lançada quando ocorre algum problema na comunicação com os WebServices da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public class LastFmException extends Exception {

	private static final long serialVersionUID = 7797201400272665724L;

	/**
	 * Cria uma nova exceção informado a operação que estava sendo executada, bem como os parâmtros utilizados, acrescentando
	 * detalhes do problema.
	 * 
	 * @param metodo
	 *            operação que estava sendo executada
	 * @param parametros
	 *            utilizados
	 * @param detalhes
	 *            do problema
	 */
	public LastFmException(Metodo metodo, Map<Parametros, String> parametros, String detalhes) {
		super(criarMensagem(metodo, parametros, detalhes));
	}

	/**
	 * 
	 * @param metodo
	 *            operação que estava sendo executada
	 * @param parametros
	 *            utilizados
	 * @param causa
	 *            do problema
	 */
	public LastFmException(Metodo metodo, Map<Parametros, String> parametros, Throwable causa) {
		super(criarMensagem(metodo, parametros, StringUtils.EMPTY), causa);
	}

	private static String criarMensagem(Metodo metodo, Map<Parametros, String> parametros, String detalhes) {
		String mensagem = MessageFormat.format("Erro ao executar operação ''{0}'' com os parâmetros ''{1}''", metodo, parametros);
		return StringUtils.isNotBlank(detalhes) ? MessageFormat.format("{0}. {1}", mensagem, detalhes) : mensagem;
	}
}
