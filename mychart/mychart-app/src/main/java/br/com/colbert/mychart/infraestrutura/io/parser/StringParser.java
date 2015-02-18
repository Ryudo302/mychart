package br.com.colbert.mychart.infraestrutura.io.parser;

/**
 * Permite a extração de dados de uma {@link String} representando algum elemento de uma parada musical.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 *
 * @param <T>
 *            o tipo de dado extraído
 */
public interface StringParser<T> extends Parser<String, T> {
}
