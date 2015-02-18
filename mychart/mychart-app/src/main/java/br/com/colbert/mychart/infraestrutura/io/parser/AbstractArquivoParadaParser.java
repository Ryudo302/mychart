package br.com.colbert.mychart.infraestrutura.io.parser;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * Implementação parcial de {@link ArquivoParadaParser} que fornece métodos utilizados por todas as implementações.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 *
 * @param <T>
 *            o tipo de dado extraído
 */
public abstract class AbstractArquivoParadaParser<T> implements ArquivoParadaParser<T>, Serializable {

	private static final long serialVersionUID = -1620371077230647965L;

	@Inject
	private transient Logger logger;

	@Override
	public final T parse(Path arquivo) {
		if (!Files.isRegularFile(Objects.requireNonNull(arquivo, "Arquivo null"))) {
			throw new IllegalArgumentException("Não é um arquivo ou não existe: " + arquivo);
		}

		logger.debug("Lendo arquivo: {}", arquivo);
		try (Stream<String> linhas = Files.lines(arquivo).filter(linha -> linha.matches(getRegex()))) {
			return parse(linhas, arquivo.toString());
		} catch (IOException exception) {
			throw new ParserException("Erro ao ler arquivo: " + arquivo, exception);
		}
	}

	/**
	 * Obtém o <em>regex</em> que determina o conteúdo a ser selecionado dentro do arquivo de texto.
	 * 
	 * @return a expressão regular
	 */
	protected abstract String getRegex();

	/**
	 * Processa as linhas do arquivo.
	 * 
	 * @param linhas
	 *            do arquivo
	 * @param caminhoArquivo
	 *            caminho do arquivo que foi lido
	 * @throws ParserException
	 *             caso ocorra algum erro durante a leitura do arquivo
	 */
	protected abstract T parse(Stream<String> linhas, String caminhoArquivo);
}
