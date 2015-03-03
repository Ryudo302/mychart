package br.com.colbert.mychart.infraestrutura.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.IntervaloDeDatas;
import br.com.colbert.mychart.dominio.importing.*;
import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.infraestrutura.io.parser.ArquivoParadaParser;

/**
 * Implementação de {@link InformacoesParadaImporter} que utiliza como fonte de dados um caminho no sistema de arquivos.
 * 
 * @author Thiago Colbert
 * @since 17/02/2015
 */
public class InformacoesParadaPathImporter implements InformacoesParadaImporter<Path>, Serializable {

	private static final long serialVersionUID = -4315570557836963800L;

	@Inject
	private Logger logger;
	@Inject
	private TopMusicalFactory topMusicalFactory;

	@Inject
	@Any
	private Instance<ArquivoParadaParser<?>> parsers;

	/**
	 * @throws NullPointerException
	 *             caso a fonte informado seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso a fonte informada não seja um diretório
	 */
	@Override
	public ParadaMusical importar(Path fonte) {
		logger.info("Importando dados de parada musical a partir dos arquivos presentes no diretório: {}", fonte);

		Objects.requireNonNull(fonte, "Fonte");
		if (!Files.isDirectory(fonte)) {
			throw new IllegalArgumentException("Não é um diretório: " + fonte);
		}

		List<TopMusical> tops = new ArrayList<>();

		try (Stream<Path> subPaths = Files.list(fonte)) {
			subPaths.filter(path -> Files.isRegularFile(path)).forEach(arquivo -> {
				logger.debug("Arquivo atual: {}", arquivo);

				for (ArquivoParadaParser<?> parser : parsers) {
					Object objeto = parser.parse(arquivo);
					System.out.println(parser.getTipoGerado().getSimpleName() + " = " + objeto);
				}
			});
		} catch (IOException exception) {
			throw new RuntimeException("Erro ao listar arquivos do caminho informado: " + fonte, exception);
		}

		return new ParadaMusical(tops);
	}
}
