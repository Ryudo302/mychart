package br.com.colbert.mychart.infraestrutura.formatter;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Um formatador de canções para exibição.
 * 
 * @author Thiago Colbert
 * @since 10/12/2014
 */
@ApplicationScoped
public class CancaoFormatter {

	private static final String SEPARADOR_ARTISTA_CANCAO = " - ";
	private static final String DELIMITADOR_TITULO_CANCAO = "\"";

	@Inject
	private transient Logger logger;

	public CancaoFormatter() {
	}

	/**
	 * Formata a canção informada.
	 * 
	 * @param cancao
	 *            a canção a ser formatada
	 * @throws NullPointerException
	 *             caso a canção informada seja <code>null</code>
	 * @return a String gerada
	 */
	public String format(Cancao cancao) {
		Objects.requireNonNull(cancao, "A canção não pode ser nula");
		logger.trace("Formatando canção para exibição: {}", cancao);

		String artistasString = new ArtistaStringBuilder().appendAll(cancao.getArtistas()).toString();
		logger.trace("Artista(s): {}", artistasString);

		return new StringBuilder().append(artistasString).append(SEPARADOR_ARTISTA_CANCAO).append(DELIMITADOR_TITULO_CANCAO)
				.append(cancao.getTitulo()).append(DELIMITADOR_TITULO_CANCAO).toString();
	}
}
