package br.com.colbert.mychart.infraestrutura.mail;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

/**
 * Classe que permite a construção de uma mensagem de e-mail.
 * 
 * @author Thiago Colbert
 * @since 01/02/2015
 */
public class MensagemEmailBuilder implements Builder<String>, Serializable {

	private static final long serialVersionUID = 767833355793407190L;

	private static final String ENCODING_PADRAO = "UTF-8";
	private static final char E_PARAMETRO = '&';

	private String destinatario;
	private List<String> copias;
	private String assunto;
	private String conteudo;
	private String anexo;

	public MensagemEmailBuilder para(String destinatario) {
		this.destinatario = destinatario;
		return this;
	}

	public MensagemEmailBuilder cc(List<String> copias) {
		this.copias = copias;
		return this;
	}

	public MensagemEmailBuilder assunto(String assunto) {
		this.assunto = assunto;
		return this;
	}

	public MensagemEmailBuilder conteudo(String conteudo) {
		this.conteudo = conteudo;
		return this;
	}

	public MensagemEmailBuilder anexo(String anexo) {
		this.anexo = anexo;
		return this;
	}

	@Override
	public String build() {
		try {
			return new StringBuilder().append("mailto:").append(destinatario).append("?subject=").append(encode(assunto))
					.append(buildCopiasString(copias)).append(E_PARAMETRO).append("body=").append(encode(conteudo))
					+ buuldAnexoString(anexo).toString();
		} catch (UnsupportedEncodingException exception) {
			// nunca deveria ocorrer
			throw new RuntimeException("Erro ao criar mensagem", exception);
		}
	}

	private String encode(String mensagem) throws UnsupportedEncodingException {
		return URLEncoder.encode(mensagem, ENCODING_PADRAO);
	}

	private String buildCopiasString(List<String> copias) {
		if (CollectionUtils.isEmpty(copias)) {
			return StringUtils.EMPTY;
		}

		StringBuilder ccBuilder = new StringBuilder();
		copias.forEach(email -> ccBuilder.append(E_PARAMETRO).append("cc=").append(email));
		return ccBuilder.toString();
	}

	private String buuldAnexoString(String anexo) {
		return StringUtils.isBlank(anexo) ? StringUtils.EMPTY : E_PARAMETRO + "attachment=" + anexo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
