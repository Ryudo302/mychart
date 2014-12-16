package br.com.colbert.base.dominio;

import java.io.Serializable;

import org.apache.commons.lang3.builder.*;

/**
 * Classe-base para as implementações de IDs compostos.
 * 
 * @author Thiago Colbert
 * @since 09/12/2014
 */
public class AbstractCompositeId implements Comparable<AbstractCompositeId>, Serializable {

	private static final long serialVersionUID = -2712413753498140122L;

	@Override
	public int compareTo(AbstractCompositeId other) {
		return CompareToBuilder.reflectionCompare(this, other);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
