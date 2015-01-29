package br.com.colbert.mychart.dominio.topmusical;

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.inject.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.*;
import org.slf4j.Logger;

import br.com.colbert.mychart.infraestrutura.info.DiretorioBase;

/**
 * Classe que centraliza todas as principais configurações de um top musical.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@Singleton
public class TopMusicalConfiguration implements Serializable {

	private static final long serialVersionUID = -5697298273142230385L;

	public static final Integer QUANTIDADE_MAXIMA_POSICOES = 100;
	public static final Frequencia[] FREQUENCIAS_PERMITIDAS = { Frequencia.DIARIO, Frequencia.SEMANAL };

	private static final String NOME_ARQUIVO_PROPRIEDADES = "chart.properties";

	private static final String PROPRIEDADE_FREQUENCIA_TOP_PRINCIPAL = "paradamusical.principal.frequencia";
	private static final String PROPRIEDADE_QUANTIDADE_POSICOES_TOP_PRINCIPAL = "paradamusical.principal.quantidadePosicoes";

	@Inject
	private Logger logger;

	@Inject
	@DiretorioBase
	private File diretorioBase;

	private File arquivoProperties;
	private Properties properties;

	// Exigência do MVP4J
	private Integer quantidadePosicoes;
	private Frequencia frequencia;

	/**
	 * Inicializa as configurações.
	 */
	@PostConstruct
	protected void init() {
		arquivoProperties = new File(diretorioBase, NOME_ARQUIVO_PROPRIEDADES);

		if (!arquivoProperties.exists()) {
			logger.debug("Copiando arquivo {} para: {}", NOME_ARQUIVO_PROPRIEDADES, arquivoProperties);
			try {
				copiarArquivoPropertiesParaDiretorioBase(arquivoProperties);
			} catch (IOException exception) {
				throw new RuntimeException("Erro ao copiar arquivo de propriedades.", exception);
			}
		}

		try {
			carregarPropriedades();
		} catch (IOException exception) {
			throw new RuntimeException("Erro ao carregar arquivo de propriedades.", exception);
		}

		quantidadePosicoes = getQuantidadePosicoes();
		frequencia = getFrequencia();
	}

	private void copiarArquivoPropertiesParaDiretorioBase(File arquivoProperties) throws IOException {
		try (InputStream arquivoOriginalStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(NOME_ARQUIVO_PROPRIEDADES)) {
			FileUtils.copyInputStreamToFile(arquivoOriginalStream, arquivoProperties);
		}
	}

	/**
	 * Salva as propriedades no arquivo.
	 *
	 * @throws IOException
	 *             caso ocorra algum erro de I/O durante a operação
	 */
	public void salvar() throws IOException {
		logger.debug("Salvando propriedades no arquivo {}: {}", arquivoProperties, properties);
		properties.store(new FileOutputStream(arquivoProperties), null);
	}

	/**
	 * Descarta todas as alterações feitas nas propriedades, recarregando seus valores a partir do arquivo já salvo.
	 *
	 * @throws IOException
	 *             caso ocorra algum erro de I/O durante a operação
	 */
	public void descartarAlteracoes() throws IOException {
		logger.debug("Descartando todas as alterações feitas");
		carregarPropriedades();
	}

	private void carregarPropriedades() throws IOException {
		try (FileInputStream inputStream = new FileInputStream(arquivoProperties)) {
			properties = new Properties();
			logger.debug("Carregando propriedades a partir do arquivo: {}", arquivoProperties);
			properties.load(inputStream);
			logger.debug("Propriedades carregadas: {}", properties);
		}
	}

	/**
	 * Obtém as frequências permitidas para um top principall
	 *
	 * @return as frequências
	 */
	public List<Frequencia> getFrequenciasPermitidas() {
		return Arrays.asList(FREQUENCIAS_PERMITIDAS);
	}

	/**
	 * Obtém a frequência dos tops principais.
	 *
	 * @return a frequência
	 */
	public Frequencia getFrequencia() {
		return Frequencia.valueOf(properties.getProperty(PROPRIEDADE_FREQUENCIA_TOP_PRINCIPAL));
	}

	public void setFrequencia(Frequencia frequencia) {
		setProperty(PROPRIEDADE_FREQUENCIA_TOP_PRINCIPAL,
				Objects.requireNonNull(frequencia.name(), "Frequência dos tops obrigatória!"));
	}

	/**
	 * Obtém as quantidades de posições permitidas em um top principal.
	 *
	 * @return as quantidades de posições
	 */
	public List<Integer> getQuantidadesPosicoesPermitidas() {
		List<Integer> quantidades = new ArrayList<>();

		for (int i = 0; i < QUANTIDADE_MAXIMA_POSICOES; i++) {
			quantidades.add(i);
		}

		return quantidades;
	}

	/**
	 * Obtém a quantidade de posições dos tops principais.
	 *
	 * @return a quantidade de posições
	 */
	public Integer getQuantidadePosicoes() {
		return Integer.parseInt(properties.getProperty(PROPRIEDADE_QUANTIDADE_POSICOES_TOP_PRINCIPAL));
	}

	public void setQuantidadePosicoes(Integer quantidadePosicoes) {
		setProperty(PROPRIEDADE_QUANTIDADE_POSICOES_TOP_PRINCIPAL,
				Objects.requireNonNull(quantidadePosicoes, "Quantidade de posições obrigatória!"));
	}

	private void setProperty(String nome, Object valor) {
		logger.debug("Alterando propriedade: {} = {}", nome, valor);
		properties.setProperty(nome, valor.toString());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("properties", properties).toString();
	}
}
