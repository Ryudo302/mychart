package br.com.colbert.mychart.infraestrutura.swing.model;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Representação de uma coluna dentro de um modelo de tabela {@link ObjectTableModel}.
 * 
 * @author Thiago Colbert
 * @since 23/02/2014
 * @param <T>
 *            tipo de objeto mapeado pela coluna
 */
public final class ObjectTableModelColumn<T> implements Serializable {

	private static final long serialVersionUID = 2308925973448343402L;

	private static final String PROPERTY_SEPARATOR = "\\.";

	private final String name;
	private final Class<T> columnClass;
	private final Optional<String> propertyName;

	private ObjectTableModelColumn(String name, Class<T> columnClass, Optional<String> propertyName) {
		this.name = name;
		this.columnClass = columnClass;
		this.propertyName = propertyName;
	}

	/**
	 * Método que inicia a criação de uma nova coluna.
	 * 
	 * @return objeto que precisa ter o nome da coluna definido
	 */
	public static <T> WithoutName<T> newColumn() {
		return new WithoutName<T>();
	}

	public String getName() {
		return this.name;
	}

	public Class<T> getColumnClass() {
		return this.columnClass;
	}

	/**
	 * Obtém o nome da propriedade mapeada para esta coluna. Pode ser retornado um único nome ou uma chamada encadeada de
	 * propriedades, como <code>valor1.valor2.valor3</code>.
	 * 
	 * @return o nome da propriedade mapeada pela coluna
	 * @see #getPropertyNameChain()
	 */
	public Optional<String> getPropertyName() {
		return this.propertyName;
	}

	/**
	 * <p>
	 * Obtém a cadeia de {@link String}s representando cada uma uma propriedade.
	 * </p>
	 * <p>
	 * Ex.:
	 * <ul>
	 * <li><code>"valor1.valor2.valor3"</code> -> <code>[ "valor1", "valor2", "valor3" ]</code></li>
	 * <li><code>"valor1"</code> -> <code>[ "valor1" ]</code></li>
	 * </ul>
	 * 
	 * @return array contendo as {@link String}s dos nomes das propriedades
	 */
	public Optional<String[]> getPropertyNameChain() {
		return getPropertyName().isPresent() ? Optional.of(getPropertyName().get().split(PROPERTY_SEPARATOR)) : Optional.empty();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", getName()).append("columnClass", getColumnClass())
				.append("propertyName", getPropertyName()).toString();
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de uma {@link ObjectTableModelColumn}. Esta necessita
	 * que seja definido o nome da coluna.
	 * 
	 * @author Thiago Colbert
	 * @since 11/12/2014
	 * @param <T>
	 *            tipo de objeto mapeado pela coluna
	 */
	public static class WithoutName<T> implements Serializable {

		private static final long serialVersionUID = -5456409619600385262L;

		/**
		 * Define o nome da coluna.
		 * 
		 * @param name
		 *            o nome da coluna (pode ser "", mas não <code>null</code>)
		 * @return um objeto que precisa ter o nome da propriedade mapeada pela coluna definido
		 * @throws NullPointerException
		 *             caso o nome informado seja <code>null</code>
		 */
		public WithoutPropertyValue<T> withName(String name) {
			return new WithoutPropertyValue<T>(Objects.requireNonNull(name, "O nome da coluna não pode ser null"));
		}
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de uma {@link ObjectTableModelColumn}. Esta necessita
	 * que seja definido o nome da propriedade mapeada pela coluna.
	 * 
	 * @author Thiago Colbert
	 * @since 11/12/2014
	 * @param <T>
	 *            tipo de objeto mapeado pela coluna
	 */
	public static class WithoutPropertyValue<T> implements Serializable {

		private static final long serialVersionUID = -5456409619600385262L;

		private String name;

		private WithoutPropertyValue(String name) {
			this.name = name;
		}

		/**
		 * Define o nome da propriedade mapeada pela coluna.
		 * 
		 * @param propertyName
		 *            o nome da propriedade
		 * @return um objeto que precisa ter a classe da propriedade definida
		 * @throws NullPointerException
		 *             caso o nome informado seja <code>null</code>
		 * @throws IllegalArgumentException
		 *             caso o nome informado seja uma {@link String} vazia
		 */
		public WithoutClass<T> withTheValueOf(String propertyName) {
			Validate.notBlank(propertyName, "O nome da propriedade é obrigatório");
			return new WithoutClass<T>(name, propertyName);
		}

		/**
		 * Cria uma nova coluna que irá exibir apenas o número das linhas.
		 * 
		 * @return a coluna criada
		 */
		public ObjectTableModelColumn<Integer> ordinal() {
			return new ObjectTableModelColumn<Integer>(name, Integer.class, Optional.empty());
		}
	}

	/**
	 * Classe utilizada para permitir o encadeamento de métodos na criação de uma {@link ObjectTableModelColumn}. Esta necessita
	 * que seja definida a classe da propriedade mapeada pela coluna.
	 * 
	 * @author Thiago Colbert
	 * @since 11/12/2014
	 * @param <T>
	 *            tipo de objeto mapeado pela coluna
	 */
	public static class WithoutClass<T> implements Serializable {

		private static final long serialVersionUID = -5456409619600385262L;

		private String name;
		private String propertyName;

		private WithoutClass(String name, String propertyName) {
			this.name = name;
			this.propertyName = propertyName;
		}

		/**
		 * Define a classe da propriedade mapeada pela coluna.
		 * 
		 * @param targetClass
		 *            a classe da propriedade
		 * @return a instância de coluna criada
		 * @throws NullPointerException
		 *             caso a classe informada seja <code>null</code>
		 */
		public ObjectTableModelColumn<T> type(Class<T> targetClass) {
			return new ObjectTableModelColumn<>(name, Objects.requireNonNull(targetClass, "A classe-alvo é obrigatória"),
					Optional.of(propertyName));
		}
	}
}
