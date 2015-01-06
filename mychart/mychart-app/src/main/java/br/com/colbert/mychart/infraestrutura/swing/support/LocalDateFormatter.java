package br.com.colbert.mychart.infraestrutura.swing.support;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * Implementação de {@link AbstractFormatter} utilizado para valores do tipo {@link LocalDate}.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
public class LocalDateFormatter extends MaskFormatter {

	private static final long serialVersionUID = 4713622658923090488L;

	private static final String FORMATO_DATAS = "dd/MM/yyyy";
	private static final String MASCARA_DATAS = "##/##/####";

	@Inject
	private Logger logger;

	@PostConstruct
	protected void init() throws ParseException {
		setMask(MASCARA_DATAS);
		setPlaceholderCharacter('0');
		setAllowsInvalid(false);
		setOverwriteMode(true);
	}

	@Override
	public Object stringToValue(String text) throws ParseException {
		logger.trace("Convertendo para LocalDate: {}", text);

		if (StringUtils.isBlank(text)) {
			return null;
		} else {

			try {
				return LocalDate.from(DateTimeFormatter.ofPattern(FORMATO_DATAS).parse(text));
			} catch (DateTimeException exception) {
				logger.debug("Data inválida: " + text, exception);
				return null;
			}
		}
	}

	@Override
	public String valueToString(Object value) throws ParseException {
		logger.trace("Convertendo para String: {}", value);
		return value != null ? ((LocalDate) value).format(DateTimeFormatter.ofPattern(FORMATO_DATAS)) : StringUtils.EMPTY;
	}
}
