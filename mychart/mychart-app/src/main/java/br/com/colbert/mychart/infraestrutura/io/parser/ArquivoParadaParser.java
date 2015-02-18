package br.com.colbert.mychart.infraestrutura.io.parser;

import java.nio.file.Path;

/**
 * Permite a leitura de um arquivo de parada musical para a extração de um determinado tipo de dado.
 * 
 * @author Thiago Colbert
 * @since 17/02/2015
 * 
 * @param <T>
 *            o tipo de dado extraído
 */
public interface ArquivoParadaParser<T> extends Parser<Path, T> {
}
