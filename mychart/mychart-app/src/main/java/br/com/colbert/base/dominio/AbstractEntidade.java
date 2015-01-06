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
		return getId() == null && other.getId() == null ? compareToNullId(other) : new CompareToBuilder().append(getId(),
				other.getId()).toComparison();
	}

	/**
	 * Compara duas entidades que não possuam ID definido.
	 * 
	 * @param other
	 *            a outra entidade
	 * @return o resultado da comparação
	 */
	protected int compareToNullId(Entidade<ID> other) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractEntidade) {
			Entidade<ID> other = (Entidade<ID>) obj;
			return getId() == null && other.getId() == null ? equalsWithNullId(other) : new EqualsBuilder().append(getId(),
					other.getId()).isEquals();
		} else {
			return false;
		}
	}

	/**
	 * Verifica se a entidade é igual a outra, sendo que ambas não possuem ID definido.
	 * 
	 * @param other
	 *            a outra entidade
	 * @return <code>true</code> caso as entidades sejam iguais, <code>false</code> caso contrário
	 */
	protected boolean equalsWithNullId(Entidade<ID> other) {
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? new HashCodeBuilder(17, 37).append(getId()).toHashCode() : hashCodeWithNullId();
	}

	/**
	 * Calcula o <em>hash code</em> da entidade no caso em que ela não possua ID definido.
	 * 
	 * @return o <em>hash code</em> calculado
	 */
	protected int hashCodeWithNullId() {
		return new HashCodeBuilder().toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", getId()).toString();
	}
}
