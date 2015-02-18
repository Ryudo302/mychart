package br.com.colbert.mychart.infraestrutura.io.parser;

import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Identifica os artistas em uma {@link String} representando uma posição de parada musical.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 */
public class ArtistaParser extends AbstractStringParser<List<Artista>> {

	private static final long serialVersionUID = 8990235037972743696L;

	private static final String NAO_DIGITO = "\\D";
	private static final String NAO_COLCHETE_DIREITO = "(?!\\])";
	private static final String QUALQUER_PALAVRA = "\\w";
	private static final String UM_OU_MAIS_QUALQUER_PALAVRA = NAO_DIGITO + "(?:" + QUALQUER_PALAVRA + ")+";
	private static final String UM_ESPACO_EM_BRANCO = "\\s";
	private static final String UM_OU_NENHUM_ESPACO_EM_BRANCO = "\\s?";
	private static final String NOME_ARTISTA = UM_OU_MAIS_QUALQUER_PALAVRA + UM_OU_NENHUM_ESPACO_EM_BRANCO;
	private static final String UM_OU_MAIS_NOMES_ARTISTAS = "(?:" + NOME_ARTISTA + ")+";

	private static final String SEPARADORES_ARTISTAS = "feat\\.|\\&|\\,|\\[feat\\.|presents|Duet With|intro\\.";
	private static final String NENHUM_OU_VARIOS_SEPARADORES_ARTISTAS = "(?:" + SEPARADORES_ARTISTAS + ")*";

	// TODO Não separando "feat." corretamente
	private static final String UM_OU_MAIS_ARTISTAS = "(?:" + UM_OU_MAIS_NOMES_ARTISTAS + NENHUM_OU_VARIOS_SEPARADORES_ARTISTAS
			+ UM_ESPACO_EM_BRANCO + NAO_COLCHETE_DIREITO + ")+";

	private static final String ARTISTA_REGEX = "(" + UM_OU_MAIS_ARTISTAS + ")";

	@Inject
	private transient Logger logger;

	private Pattern pattern;

	@PostConstruct
	protected void init() {
		pattern = Pattern.compile(ARTISTA_REGEX, Pattern.CASE_INSENSITIVE);
	}

	@Override
	protected Pattern getPattern() {
		return pattern;
	}

	@Override
	protected List<Artista> convert(String group) {
		List<Artista> artistas = new ArrayList<>();
		String[] nomesArtistas = group.split(SEPARADORES_ARTISTAS);

		for (String nomeArtista : nomesArtistas) {
			artistas.add(new Artista(nomeArtista.trim()));
		}

		logger.debug("Artistas: {}", artistas);
		
		// FIXME
		artistas.add(null);

		return artistas;
	}
}
