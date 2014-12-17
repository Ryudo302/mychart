package br.com.colbert.base.dominio;

import java.io.Serializable;

import org.apache.commons.lang3.builder.*;

/**
 * Base de implementação de todas as entidades persistentes da aplicação.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 * 
 * @param <ID>
 *            tipo do identificador único da entidade
 */
public abstract class AbstractEntidade<ID extends Serializable> implements Entidade<ID> {

	private static final long serialVersionUID = -4271205291225714188L;

	@Override
	public int compareTo(Entidade<ID> other) {
		return new CompareToBuilder().append(getId(), other.getId()).toComparison();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return obj instanceof AbstractEntidade && new EqualsBuilder().append(getId(), ((Entidade<ID>) obj).getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", getId()).toString();
	}
}
