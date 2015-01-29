package br.com.colbert.mychart.infraestrutura.util;

import java.util.Objects;

/**
 * Classe utilitária para operações envolvendo {@link Enum}s.
 * 
 * @author Thiago Colbert
 * @since 29/01/2015
 */
public final class EnumUtils {

	private EnumUtils() {

	}

	/**
	 * <p>
	 * Cria uma {@link String} representando um {@link Enum} de uma forma <em>user-friendly</em>, a partir do nome da constante
	 * enum.
	 * </p>
	 * <p>
	 * <strong>Exemplos</strong>:
	 * <ul>
	 * <li><code>TESTE_TESTE</code> -> <code>"Teste Teste"</code></li>
	 * <li><code>UM_VALOR_QUALQUER</code> -> <code>"Um Valor Qualquer"</code></li>
	 * <li><code>NOME_DO_CLIENTE</code> -> <code>"Nome Do Cliente"</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @param enumeration
	 *            o objeto enum
	 * @return a String formatada representando o enum
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code> como parâmetro
	 */
	public static String toFormattedString(Enum<?> enumeration) {
		String[] splitNames = Objects.requireNonNull(enumeration, "Enum não informado").name().toLowerCase().split("_");
		StringBuffer fixedName = new StringBuffer();

		for (int i = 0; i < splitNames.length; i++) {
			String firstLetter = splitNames[i].substring(0, 1).toUpperCase(), restOfWord = splitNames[i].substring(1), spacer = i == splitNames.length ? ""
					: " ";

			fixedName.append(firstLetter).append(restOfWord).append(spacer);
		}

		return fixedName.toString().trim();
	}
}
