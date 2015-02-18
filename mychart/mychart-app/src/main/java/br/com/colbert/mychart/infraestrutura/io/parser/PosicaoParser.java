package br.com.colbert.mychart.infraestrutura.io.parser;

import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.topmusical.Posicao;

/**
 * Permite a identificação de posições de um top musical a partir de um arquivo textual.
 * 
 * @author Thiago Colbert
 * @since 18/02/2015
 */
public class PosicaoParser extends AbstractArquivoParadaParser<List<Posicao>> {

	private static final long serialVersionUID = -7571712622062071401L;

	private static final String CANCAO_REGEX = "";
	private static final String MOVIMENTACAO_REGEX = "";
	private static final String PERMANENCIA_REGEX = "";
	private static final String NUMERO_MELHOR_POSICAO_REGEX = "";
	private static final String PERMANENCIA_MELHOR_POSICAO_REGEX = "";
	private static final String MELHOR_POSICAO_REGEX = "";
	private static final String POSICAO_REGEX = ".*((\\d{2}\\/){2}\\d{4} ~ ((\\d{2}\\/){2}\\d{4})?).*";

	@Inject
	private transient Logger logger;

	@Inject
	private NumeroPosicaoParser numeroPosicaoParser;
	@Inject
	private ArtistaParser artistaParser;

	@Override
	protected String getRegex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Posicao> parse(Stream<String> linhas, String arquivo) {
		// TODO Auto-generated method stub
		return null;
	}
}
