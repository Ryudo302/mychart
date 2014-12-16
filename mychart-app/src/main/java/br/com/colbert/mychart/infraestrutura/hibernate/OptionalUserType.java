package br.com.colbert.mychart.infraestrutura.hibernate;

import java.io.Serializable;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.*;

/**
 * Implementação de {@link UserType} que informa ao Hibernate como tratar dados do tipo {@link Optional}.
 * 
 * @author Thiago Colbert
 * @since 16/12/2014
 * @see <a href="http://stackoverflow.com/questions/10604865/mapping-a-functionaljava-optiontype-with-hibernate">Mapping a
 *      FunctionalJava Option<Type> with Hibernate</a>
 */
public class OptionalUserType implements EnhancedUserType, Serializable {

	private static final long serialVersionUID = 7184685724811619385L;

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.NULL };
	}

	@Override
	public Class<?> returnedClass() {
		return Optional.class;
	}

	@Override
	public boolean equals(Object o, Object o2) throws HibernateException {
		return Objects.equals(o, o2);

	}

	@Override
	public int hashCode(Object o) throws HibernateException {
		assert (o != null);
		return o.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		return Optional.of(resultSet.getObject(names[0]));
	}

	void handleDate(PreparedStatement preparedStatement, Date optionalValue, int index) throws SQLException {
		preparedStatement.setDate(index, optionalValue);
	}

	void handleNumber(PreparedStatement preparedStatement, String stringValue, int index) throws SQLException {
		Double doubleValue = Double.valueOf(stringValue);
		preparedStatement.setDouble(index, doubleValue);
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {

		if (value != null) {
			if (value instanceof Optional) {
				Optional<?> optional = (Optional<?>) value;
				if (optional.isPresent()) {
					Object optionalValue = optional.get();
					if (optionalValue instanceof LocalDate) {
						handleDate(preparedStatement, toDate((LocalDate) optionalValue), index);
					} else {
						String stringValue = String.valueOf(optionalValue);

						if (StringUtils.isNotBlank(stringValue)) {
							if (StringUtils.isNumeric(stringValue)) {
								handleNumber(preparedStatement, stringValue, index);
							} else {
								preparedStatement.setString(index, optionalValue.toString());
							}
						} else {
							preparedStatement.setString(index, null);
						}
					}
				}
			}
		} else {
			preparedStatement.setString(index, null);
		}
	}

	private Date toDate(LocalDate localDate) {
		return new Date(localDate.get(ChronoField.MILLI_OF_SECOND));
	}

	@Override
	public Object deepCopy(Object o) throws HibernateException {
		return o;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object o) throws HibernateException {
		return (Serializable) o;
	}

	@Override
	public Object assemble(Serializable serializable, Object o) throws HibernateException {
		return serializable;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public String objectToSQLString(Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toXMLString(Object object) {
		return object.toString();
	}

	@Override
	public Object fromXMLString(String string) {
		return Optional.of(string);
	}
}
