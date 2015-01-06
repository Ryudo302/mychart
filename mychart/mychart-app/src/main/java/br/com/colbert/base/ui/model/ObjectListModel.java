package br.com.colbert.base.ui.model;

import java.util.*;

import javax.swing.*;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.*;

/**
 * Implementação de {@link ListModel} para elementos genéricos.
 * 
 * @author Thiago Colbert
 * @param <E>
 *            o tipo de elemento mantido na lista
 * @since 25/02/2014
 */
public class ObjectListModel<E> extends AbstractListModel<E> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectListModel.class);

	private List<E> elements;

	/**
	 * Cria um novo modelo com os elementos informados.
	 * 
	 * @param elements
	 *            os elementos a serem adicionados ao modelo
	 */
	public ObjectListModel(Collection<E> elements) {
		this.elements = new ArrayList<E>(Objects.requireNonNull(elements, "A coleção de elementos não pode ser null"));
	}

	/**
	 * Cria um novo modelo vazio.
	 */
	public ObjectListModel() {
		this(CollectionUtils.emptyCollection());
	}

	@Override
	public E getElementAt(int index) {
		LOGGER.trace("Retornando elemento na posição {}", index);
		return this.elements.get(index);
	}

	public E setElementAt(int index, E element) {
		LOGGER.trace("Alterando elemento na posição {} para {}", index, element);
		E oldElement = this.elements.set(index, element);
		fireContentsChanged(this, index, index);
		return oldElement;
	}

	@Override
	public int getSize() {
		return this.elements.size();
	}

	public List<E> getElements() {
		return new ArrayList<E>(elements);
	}

	public void setElements(Collection<E> elements) {
		LOGGER.trace("Alterando elementos para: {}", elements);
		this.clear();

		if (CollectionUtils.isNotEmpty(elements)) {
			this.elements.addAll(elements);
		}

		LOGGER.trace("Notificando ouvintes");
		this.fireContentsChanged(this, 0, getSize() > 0 ? getSize() - 1 : 0);
	}

	public boolean addElement(E element) {
		LOGGER.trace("Adicionando elemento: {}", element);

		int index = getSize();
		boolean added = this.elements.add(element);

		if (added) {
			fireIntervalAdded(this, index, index);
		}

		return added;
	}

	public void addElementAt(int index, E element) {
		LOGGER.trace("Adicionando elemento na posição {}: {}", index, element);
		this.elements.add(index, element);
		fireContentsChanged(this, index, getSize() - 1);
	}

	public boolean removeElement(E element) {
		boolean removed = true;
		int index = elements.indexOf(element);

		if (index != -1) {
			removed = elements.remove(element);
			fireIntervalRemoved(this, index, index);
		}

		return removed;
	}

	public void removeAllElements(Collection<E> elements) {
		LOGGER.trace("Removendo elementos: {}", elements);

		if (CollectionUtils.isNotEmpty(elements)) {
			for (E element : elements) {
				removeElement(element);
			}
		}
	}

	public void clear() {
		LOGGER.trace("Limpando lista");
		int oldSize = getSize();
		this.elements.clear();
		this.fireIntervalRemoved(this, 0, oldSize);
	}

	public boolean isEmpty() {
		return getSize() == 0;
	}

	public void moveDown(int index) {
		LOGGER.trace("Movendo elemento no índice {} para baixo ({})", index, index + 1);

		if (index + 1 < getSize()) {
			E firstElement = this.elements.get(index);
			E secondElement = this.elements.get(index + 1);
			setElementAt(index, secondElement);
			setElementAt(index + 1, firstElement);
			fireContentsChanged(this, index, index + 1);
		}
	}

	public void moveUp(int index) {
		LOGGER.trace("Movendo elemento no índice {} para cima ({})", index, index - 1);

		if (index - 1 >= 0) {
			E firstElement = this.elements.get(index);
			E secondElement = this.elements.get(index - 1);
			setElementAt(index, secondElement);
			setElementAt(index - 1, firstElement);
			fireContentsChanged(this, index - 1, index);
		}
	}
}
