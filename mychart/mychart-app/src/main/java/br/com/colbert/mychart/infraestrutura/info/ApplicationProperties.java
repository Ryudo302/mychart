package br.com.colbert.mychart.infraestrutura.info;

import java.io.*;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao.Formato;

/**
 * Classe que provê acesso às informações da aplicação.
 * 
 * @author Thiago Colbert
 * @since 18/12/2014
 */
@ApplicationScoped
public class ApplicationProperties implements Serializable {

	private static final long serialVersionUID = 2808347955265751909L;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("application");

	private transient String tituloCompleto;

	public ApplicationProperties() {
		tituloCompleto = new StringBuilder().append(getNomeApp()).append(StringUtils.SPACE).append("v.")
				.append(BUNDLE.getString("app.version")).append(StringUtils.SPACE).append(BUNDLE.getString("app.build"))
				.toString();
	}

	/**
	 * Obtém o nome da aplicação.
	 * 
	 * @return o nome
	 */
	@Produces
	@TituloAplicacao(Formato.APENAS_NOME)
	public String getNomeApp() {
		return BUNDLE.getString("app.nome");
	}

	/**
	 * Obtém uma {@link String} formatada contendo o nome, número de versão e número de <em>build</em> da aplicação.
	 * 
	 * @return a String gerada
	 */
	@Produces
	@TituloAplicacao(Formato.COMPLETO)
	public String getAppNomeVersaoBuild() {
		return tituloCompleto;
	}

	/**
	 * Obtém uma referência para o diretório base utilizado pela aplicação no sistema de arquivos do usuário.
	 * 
	 * @param nomeApp
	 *            o nome da aplicação
	 * @return o diretório base da aplicação
	 */
	@Produces
	@DiretorioBase
	public File getDiretorioBase(@TituloAplicacao(Formato.APENAS_NOME) String nomeApp) {
		return new File(FileUtils.getUserDirectory(), '.' + nomeApp.toLowerCase());
	}

	/**
	 * Obtém uma lista dos e-mails dos desenvolvedores da aplicação.
	 * 
	 * @return os endereços de e-mail
	 */
	@Produces
	@EmailsDesenvolvedores
	public List<String> getEmailsDesenvolvedores() {
		return Arrays.asList("th.colbert@gmail.com");
	}
}
