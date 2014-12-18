package br.com.colbert.mychart.infraestrutura.info;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.enterprise.inject.Produces;

import org.apache.commons.lang3.StringUtils;

import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao.Formato;

/**
 * Classe que provê acesso às informações da aplicação.
 * 
 * @author Thiago Colbert
 * @since 18/12/2014
 */
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
}
