package br.com.colbert.mychart.infraestrutura.jpa.converter;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.*;

/**
 * {@link AttributeConverter} utilizado para converter valores do tipo {@link LocalDate} para {@link Date} e vice-versa.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 * @see <a href="http://stackoverflow.com/questions/23890687/hibernate-4-with-java-time-localdate-and-date-construct">Hibernate 4
 *      with java.time.LocalDate and DATE() construct</a>
 */
@Converter
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, Date>, Serializable {

	private static final long serialVersionUID = -4626293424948731887L;

	@Override
	public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
		if (entityValue != null) {
			return Date.valueOf(entityValue);
		}

		return null;
	}

	@Override
	public LocalDate convertToEntityAttribute(Date databaseValue) {
		if (databaseValue != null) {
			return databaseValue.toLocalDate();
		}

		return null;
	}
}
