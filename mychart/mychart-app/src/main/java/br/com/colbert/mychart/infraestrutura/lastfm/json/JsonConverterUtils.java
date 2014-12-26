package br.com.colbert.mychart.infraestrutura.lastfm.json;

import java.util.Map;

import com.google.gson.JsonParseException;

/**
 * Operações utilitárias para conversores de JSON.
 * 
 * @author Thiago Colbert
 * @since 25/12/2014
 */
public final class JsonConverterUtils {

	private JsonConverterUtils() {

	}

	/**
	 * Obtém o valor de uma chave em um mapa se existir ou lança uma exceção caso a chave não exista.
	 * 
	 * @param map
	 *            o mapa
	 * @param key
	 *            a chave a ser buscada
	 * @return o valor no mapa com a chave informada (pode ser <code>null</code> caso o mapa informado permita valores nulos)
	 * @throws JsonParseException
	 *             caso a chave não seja encontrada no mapa
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getOrThrowExceptionIfNotExists(Map<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (T) map.get(key);
		} else {
			throw new JsonParseException("Propriedade esperada mas não encontrada: '" + key + "'");
		}
	}
}
