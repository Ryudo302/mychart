package br.com.colbert.base.ui.model;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.table.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.*;

/**
 * <p>
 * Implementação de {@link TableModel} utilizada para representar objetos genéricos.
 * </p>
 * <p>
 * Esta classe utiliza alguns termos que estão descritos abaixo:
 * <ul>
 * <li><strong>Célula</strong>: valor representado dentro de um par linha/coluna do modelo.</li>
 * <li><strong>Elemento</strong>: objeto representado por uma linha qualquer do modelo.</li>
 * </ul>
 * </p>
 *
 * @author Thiago Colbert
 * @since 23/02/2014
 * @param <T>
 *            tipo de objeto representado pelo modelo
 */
public abstract class ObjectTableModel<T extends Comparable<? super T>> extends AbstractTableModel {

	private static final long serialVersionUID = -786589357042752679L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectTableModel.class);

	private List<T> elements;
	private final List<ObjectTableModelColumn<?>> columns;
	private boolean editable;

	public ObjectTableModel(List<T> elements, boolean editable) {
		this.elements = new ArrayList<T>(elements);
		this.columns = makeColumns();
		this.editable = editable;
	}

	public ObjectTableModel(List<T> elements) {
		this(elements, false);
	}

	public ObjectTableModel(boolean editable) {
		this(Collections.emptyList(), editable);
	}

	public ObjectTableModel() {
		this(Collections.emptyList(), false);
	}

	/**
	 * Cria as colunas da tabela, representadas por instâncias de {@link ObjectTableModelColumn}. As colunas serão adicionadas ao
	 * modelo na ordem em que estiverem presentes na lista.
	 *
	 * @return a lista de colunas da tabela, em ordem de exibição
	 */
	protected abstract List<ObjectTableModelColumn<?>> makeColumns();

	/**
	 * Adicionada um elemento ao modelo. Caso o elemento já exista na tabela, ele será atualizado.
	 *
	 * @param element
	 *            elemento a ser adicionado ao modelo
	 */
	public void addElement(T element) {
		if (!this.containsElement(element)) {
			// inserindo
			LOGGER.trace("Inserindo elemento: " + element);
			this.elements.add(element);
		} else {
			// atualizando
			LOGGER.trace("Atualizando elemento: " + element);
			int index = this.elements.indexOf(element);
			this.elements.set(index, element);
		}

		LOGGER.trace("Ordenando elementos por sua ordem natural");
		Collections.sort(elements);

		fireTableDataChanged();
	}

	/**
	 * Adiciona todos os elementos informados ao modelo. Caso algum dos elementos já exista, ele será atualizado.
	 *
	 * @param elements
	 *            os elementos a serem adicionados
	 */
	public void addAllElements(List<T> elements) {
		elements.forEach(element -> addElement(element));
	}

	/**
	 * Verifica se um elemento existe dentro do modelo.
	 *
	 * @param element
	 *            elemento a ser verificado
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean containsElement(T element) {
		return this.elements.contains(element);
	}

	/**
	 * Remove um elemento do modelo. Caso o elemento não exista no modelo, este método não fará nada.
	 *
	 * @param element
	 *            elemento a ser removido
	 * @return <code>true</code> caso um elemento tenha sido removido do modelo, <code>false</code> caso contrário
	 */
	public boolean removeElement(T element) {
		boolean elementoExiste = elements.indexOf(element) != -1;
		if (elementoExiste) {
			LOGGER.trace("Removendo elemento da tabela: " + element);
			this.elements.remove(element);
			fireTableRowsDeleted(elements.indexOf(element), elements.indexOf(element));
		}

		return elementoExiste;
	}

	/**
	 * Remove todos os elementos do modelo cuja propriedade informada tenha o valor informado.
	 * 
	 * @param propertyName
	 *            o nome da propriedade
	 * @param propertyValue
	 *            o valor da propriedade
	 * @throws NullPointerException
	 *             caso o nome ou o valor da propriedade sejam <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o nome da propriedade seja uma String vazia
	 * @return a quantidade de elementos que foram removidos do modelo
	 */
	public int removeElementByProperty(String propertyName, Object propertyValue) {
		Validate.notEmpty(propertyName, "Nome da propriedade não informado");
		Objects.requireNonNull(propertyValue, "Valor da propriedade não informado");

		LOGGER.trace("Identificando elementos que atendam ao critério: {} = {}", propertyName, propertyValue);
		List<T> elementsToRemove = this.elements.stream()
				.filter(element -> getValue(element, propertyName).equals(propertyValue)).collect(Collectors.toList());
		LOGGER.trace("Elementos identificados e que serão removidos: {}", elementsToRemove);

		return removeAll(elementsToRemove);
	}

	/**
	 * Remove uma coleção de elementos do modelo.
	 *
	 * @param elements
	 *            elementos a serem removidos
	 * @return a quantidade de elementos que foram removidos do modelo
	 */
	public int removeAll(List<T> elements) {
		LOGGER.trace("Removendo todos os elementos: {}", elements);
		int quantidadeInicialElementos = getRowCount();
		this.elements.removeAll(elements);
		fireTableDataChanged();
		int quantidadeFinalElementos = getRowCount();
		assert quantidadeFinalElementos - quantidadeInicialElementos >= 0;
		return quantidadeFinalElementos - quantidadeInicialElementos;
	}

	/**
	 * Atualiza os valores das propriedades de um elemento no modelo.
	 * 
	 * @param element
	 *            o elemento a ser atualizado
	 * @return o elemento que foi atualizado, antes de ser atualizado
	 */
	public T refresh(T element) {
		LOGGER.debug("Refresh: {}", element);
		int index = this.elements.indexOf(element);
		T updatedElement = this.elements.set(index, element);
		fireTableRowsUpdated(index, index);
		return updatedElement;
	}

	/**
	 * Obtém um elemento do modelo a partir do número da linha que o representa.
	 *
	 * @param rowIndex
	 *            índice da linha que representa o elemento desejado
	 * @return o elemento na linha informada
	 * @throws IndexOutOfBoundsException
	 *             caso seja informado umíndice de linha inválido (que seja < 0 ou >= {@link #getRowCount()})
	 */
	public T getElement(int rowIndex) {
		return elements.get(rowIndex);
	}

	/**
	 * Obt�m uma lista com os elementos contidos no modelo.
	 *
	 * @return uma lista imutável (<code>view</code>) dos elementos do modelo
	 */
	public List<T> getElements() {
		return Collections.unmodifiableList(elements);
	}

	/**
	 * Define os elementos contidos dentro do modelo. Os elementos que possam estar contidos no modelo antes da execução deste
	 * método seráo descartados.
	 *
	 * @param elements
	 *            elementos a serem adicionados ao modelo
	 */
	public void setElements(Collection<T> elements) {
		clear();

		if (CollectionUtils.isNotEmpty(elements)) {
			LOGGER.trace("Adicionando elementos à tabela: {}", elements);
			this.elements.addAll(elements);
		}

		fireTableDataChanged();
	}

	/**
	 * Remove todos os elementos do modelo.
	 */
	public void clear() {
		LOGGER.trace("Limpando tabela");
		this.elements.clear();
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return getColumn(columnIndex).getName();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getColumn(columnIndex).getColumnClass();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public int getRowCount() {
		return elements.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Optional<String[]> optionalProperties = getColumn(columnIndex).getPropertyNameChain();
		if (optionalProperties.isPresent()) {
			String[] properties = optionalProperties.get();
			return getPropertyChainValue(rowIndex, properties);
		} else {
			return rowIndex + 1;
		}
	}

	private Object getPropertyChainValue(int rowIndex, String[] properties) {
		Object value = getElement(rowIndex);

		for (String currentProperty : properties) {
			LOGGER.trace("Obtendo valor da propriedade: " + currentProperty);
			value = getValue(value, currentProperty);
		}

		return value;
	}

	private <OBJETO, VALOR> VALOR getValue(OBJETO element, String property) {
		return invokeMethod(element, "get" + StringUtils.capitalize(property));
	}

	/**
	 * Verifica se determinada célula do modelo é ediável. Por padrão, este método retorna o mesmo que {@link #isEditable()}.
	 *
	 * @return <code>true</code>/<code>false</code>
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return isEditable();
	}

	/**
	 * Verifica se o modelo é <em>editável</em>, ou seja, se os valores de suas células podem ser alterados individualmente.
	 *
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Define um novo valor para uma determinada célula do modelo. Este método só será executado caso
	 * {@link #isCellEditable(int, int)} retorne <code>true</code> para a célula informada.
	 */
	@Override
	public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
		if (isCellEditable(rowIndex, columnIndex)) {
			getColumn(columnIndex).getPropertyName().ifPresent(
					propertyName -> invokeMethod(getElement(rowIndex), "set" + StringUtils.capitalize(propertyName), newValue));
		}
	}

	@SuppressWarnings("unchecked")
	private <OBJETO, VALOR> VALOR invokeMethod(OBJETO element, String methodName, Object... args) {
		try {
			return (VALOR) MethodUtils.invokeMethod(element, methodName, args);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
			throw new IllegalArgumentException("Erro ao invocar o método " + methodName + " do objeto " + element
					+ " com o(s) parâmetro(s): " + args, exception);
		}
	}

	/**
	 * Obt�m a instância de {@link ObjectTableModelColumn} representando uma determinada coluna do modelo.
	 *
	 * @param columnIndex
	 *            índice da coluna desejada
	 * @return a instância da coluna
	 */
	@SuppressWarnings("unchecked")
	protected <C> ObjectTableModelColumn<C> getColumn(int columnIndex) {
		return (ObjectTableModelColumn<C>) columns.get(columnIndex);
	}
}
