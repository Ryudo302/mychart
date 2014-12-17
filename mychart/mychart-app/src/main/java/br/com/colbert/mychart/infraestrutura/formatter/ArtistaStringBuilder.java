package br.com.colbert.mychart.infraestrutura.formatter;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;

import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * <p>
 * Classe reponsável por gerar uma {@link String} de exibição de um conjunto de {@link Artista}s.
 * </p>
 * <p>
 * Exemplos:
 * <ul>
 * <li>[A] -> A</li>
 * <li>[A,B] -> A feat. B</li>
 * <li>[A,B,C] -> A feat. B &amp; C</li>
 * <li>[A,B,C,D] -> A feat. B, C &amp; D</li>
 * </ul>
 * </p>
 * 
 * @author ThiagoColbert
 * @since 24/02/2014
 */
public final class ArtistaStringBuilder implements Builder<String>, Serializable {

	private static final long serialVersionUID = 8130848270452595231L;

	private static final String FEATURING = "feat.";
	private static final String SEPARATOR = ",";
	private static final String AND = "&";

	private List<String> texto;
	private int indiceAnd;

	public ArtistaStringBuilder() {
		this.texto = new ArrayList<>();
		this.indiceAnd = -1;
	}

	/**
	 * Obtém os nomes de artistas presentes em uma {@link String} que siga o padrão utilizado por esta classe.
	 * 
	 * @param string
	 * @return os nomes dos artistas, a mesma string informada caso não possua separadores de nomes de artistas ou
	 *         <code>null</code> caso a string informada seja <code>null</code> ou vazia
	 */
	public static String[] getNomesArtistas(String string) {
		if (StringUtils.isBlank(string)) {
			return null;
		} else {
			String[] nomesArtistas = string.split(FEATURING + "|" + SEPARATOR + "|" + AND);
			return removerEspacosVazios(nomesArtistas);
		}
	}

	private static String[] removerEspacosVazios(String[] array) {
		String[] novoArray = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			novoArray[i] = array[i].trim();
		}
		return novoArray;
	}

	/**
	 * Adiciona um {@link Artista} ao builder.
	 * 
	 * @param artista
	 *            a ser adicionado
	 * @return o próprio builder, para chamadas de métodos encadeados
	 * @see #appendAll(Collection)
	 */
	public ArtistaStringBuilder append(Artista artista) {
		if (texto.isEmpty()) {
			texto.add(artista.getNome());
		} else if (texto.size() == 1) {
			texto.add(FEATURING);
			texto.add(artista.getNome());
		} else {
			if (indiceAnd != -1) {
				texto.set(indiceAnd, SEPARATOR);
			}
			texto.add(AND);
			indiceAnd = texto.size() - 1;
			texto.add(artista.getNome());
		}

		return this;
	}

	/**
	 * Adiciona todos os {@link Artista}s presentes na coleção informada.
	 * 
	 * @param artistas
	 *            os artistas a serem adicionados
	 * @return o próprio builder, para chamadas de métodos encadeados
	 * @see #append(Artista)
	 */
	public ArtistaStringBuilder appendAll(Collection<Artista> artistas) {
		if (CollectionUtils.isNotEmpty(artistas)) {
			for (Artista artista : artistas) {
				append(artista);
			}
		}

		return this;
	}

	@Override
	public String build() {
		StringBuilder builder = new StringBuilder();

		for (String currentElement : texto) {
			if (!currentElement.equals(SEPARATOR)) {
				builder.append(StringUtils.SPACE);
			}
			
			builder.append(currentElement);
		}

		return builder.toString().trim();
	}

	@Override
	public String toString() {
		return build();
	}
}
