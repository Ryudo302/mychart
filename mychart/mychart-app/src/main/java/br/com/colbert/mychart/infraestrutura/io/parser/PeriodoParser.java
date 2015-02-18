package br.com.colbert.mychart.infraestrutura.io.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;
import java.util.regex.*;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.IntervaloDeDatas;

/**
 * Permite a identificação do período de uma parada musical a partir de um arquivo textual.
 *
 * @author Thiago Colbert
 * @since 17/02/2015
 */
public class PeriodoParser extends AbstractArquivoParadaParser<IntervaloDeDatas> {

	private static final long serialVersionUID = 2688844929853789288L;

	private static final String PERIODO_REGEX = ".*((\\d{2}\\/){2}\\d{4} ~ ((\\d{2}\\/){2}\\d{4})?).*";
	private static final String DATA_PATTERN = "dd/MM/yyyy";

	private static final String SEPARADOR_DATAS = " ~ ";

	@Inject
	private transient Logger logger;

	private Pattern pattern;
	private transient DateTimeFormatter formatter;

	@PostConstruct
	protected void init() {
		pattern = Pattern.compile(PERIODO_REGEX);
		formatter = DateTimeFormatter.ofPattern(DATA_PATTERN);

		logger.debug("Regex: {}", pattern);
		logger.debug("Formatador de datas: {}", formatter);
	}

	@Override
	protected String getRegex() {
		return PERIODO_REGEX;
	}

	@Override
	protected IntervaloDeDatas parse(Stream<String> linhas, String caminhoArquivo) {
		Matcher matcher = pattern.matcher(linhas.findFirst().orElseThrow(new Supplier<ParserException>() {
			@Override
			public ParserException get() {
				return new ParserException("Não foi possível identificar o período do top musical: " + caminhoArquivo);
			}
		}));
		matcher.matches(); // sempre true
		String periodo = matcher.group(1);
		logger.debug("Período identificado: {}", periodo);

		String[] datas = periodo.split(SEPARADOR_DATAS);
		LocalDate dataInicio = LocalDate.parse(datas[0], formatter);
		LocalDate dataFim = LocalDate.parse(datas[1], formatter);

		return IntervaloDeDatas.novo().de(dataInicio).ate(dataFim);
	}
}
