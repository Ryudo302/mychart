package br.com.colbert.mychart.ui.comum.messages;

import java.io.Serializable;

import org.apache.commons.lang3.builder.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 
 * @author Thiago Colbert
 * @since 24/01/2015
 */
public class Erro implements Serializable {

	private static final long serialVersionUID = 6526974600239919791L;

	private final String mensagem;
	private final String detalhes;

	/**
	 * 
	 * @param mensagem
	 * @param detalhes
	 */
	public Erro(String mensagem, String detalhes) {
		this.mensagem = mensagem;
		this.detalhes = detalhes;
	}

	/**
	 * 
	 * @param throwable
	 */
	public Erro(Throwable throwable) {
		this(throwable.getLocalizedMessage(), ExceptionUtils.getStackTrace(throwable));
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getDetalhes() {
		return detalhes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
