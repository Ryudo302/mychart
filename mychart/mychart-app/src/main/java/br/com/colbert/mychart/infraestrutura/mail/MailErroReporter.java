package br.com.colbert.mychart.infraestrutura.mail;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.erro.*;
import br.com.colbert.mychart.infraestrutura.info.*;
import br.com.colbert.mychart.ui.comum.messages.Erro;

/**
 * Implementação de {@link ErroReporter} que enviar as notificações por e-mail.
 * 
 * @author Thiago Colbert
 * @since 01/02/2015
 */
public class MailErroReporter implements ErroReporter, Serializable {

	private static final long serialVersionUID = 2630645568613718209L;

	private static final String ASSUNTO_PADRAO = "[MyChart] Notificação de erro";

	@Inject
	private Logger logger;

	@Inject
	@EmailsDesenvolvedores
	private List<String> emailsDesenvolvedores;

	@Inject
	@DiretorioBase
	private File diretorioApp;

	@Override
	public void reportar(Erro erro) {
		if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.MAIL)
				|| GraphicsEnvironment.isHeadless()) {
			throw new ErroReporterException("O envio de e-mails não é suportado por este ambiente");
		}

		try {
			String mensagem = new MensagemEmailBuilder().para(emailsDesenvolvedores.get(0)).assunto(ASSUNTO_PADRAO)
					.conteudo(erro.getMensagem() + StringUtils.repeat(StringUtils.LF, 2) + erro.getDetalhes())
					.anexo(new File(diretorioApp, "mychart.log").getAbsolutePath()).build();
			logger.debug("Enviando mensagem: {}", mensagem);
			URI uri = URI.create(mensagem);
			logger.debug("Abrindo cliente de e-mail. URI = {}", uri);
			Desktop.getDesktop().mail(uri);
		} catch (IOException exception) {
			throw new ErroReporterException("Erro ao enviar e-mail", exception);
		}
	}
}
