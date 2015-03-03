package br.com.colbert.mychart.infraestrutura.io.parser;

import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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

	private static final String QUALQUER_CARACTERE_UM_OU_MAIS = "(.+)";
	private static final String ESPACO = "\\s";

	private static final String SEPARADORES_ARTISTAS = "feat\\.|\\&|\\,|\\[feat\\.|presents|Duet With|intro\\.";
	private static final String ARTISTA_REGEX = "\\d+" + ESPACO + QUALQUER_CARACTERE_UM_OU_MAIS + ESPACO + "-";

	@Inject
	private transient Logger logger;

	private Pattern pattern;

	@PostConstruct
	protected void init() {
		pattern = Pattern.compile(ARTISTA_REGEX);
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
		return artistas;
	}
}
