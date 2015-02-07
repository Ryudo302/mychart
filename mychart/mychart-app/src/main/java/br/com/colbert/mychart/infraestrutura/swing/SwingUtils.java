package br.com.colbert.mychart.infraestrutura.swing;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;

import br.com.colbert.mychart.infraestrutura.exception.ViewException;
import br.com.colbert.mychart.infraestrutura.swing.model.ObjectTableModel;

/**
 * Classe utilitária para operações na camada de visão com Swing.
 * 
 * @author Thiago Colbert
 * @since 08/01/2015
 */
public final class SwingUtils {

	private SwingUtils() {

	}

	/**
	 * Limpa todos os dados presentes nos componentes do contêiner informado.
	 * 
	 * @param container
	 *            o contêiner
	 */
	public static void clearAllData(Container container) {
		invokeLater(() -> {
			_clearAllInputs(container);
		});
	}

	private static void _clearAllInputs(Container container) {
		Component[] componentes = container.getComponents();
		for (Component componente : componentes) {
			if (componente instanceof JTextComponent) {
				((JTextComponent) componente).setText(StringUtils.EMPTY);
			} else if (componente instanceof JComboBox) {
				((JComboBox<?>) componente).setSelectedIndex(0);
			} else if (componente instanceof JList) {
				((JList<?>) componente).getSelectionModel().clearSelection();
			} else if (componente instanceof JToggleButton) {
				((JToggleButton) componente).setSelected(false);
				/*
				 * } else if (componente instanceof JTable) { clearJTable((JTable) componente);
				 */
			} else if (componente instanceof Container) {
				_clearAllInputs((Container) componente);
			}
		}
	}

	private static void clearJTable(JTable componente) {
		TableModel tableModel = componente.getModel();
		if (tableModel instanceof DefaultTableModel) {
			((DefaultTableModel) tableModel).setRowCount(0);
		} else if (tableModel instanceof ObjectTableModel<?>) {
			((ObjectTableModel<?>) tableModel).clear();
		} else {
			try {
				TableModel newModel = tableModel.getClass().newInstance();
				componente.setModel(newModel);
			} catch (InstantiationException | IllegalAccessException exception) {
				throw new ViewException("Erro ao limpar tabela", exception);
			}
		}
	}

	/**
	 * Obtém um único elemento selecionado na tabela informada.
	 * 
	 * @param table
	 *            a tabela
	 * @return o elemento selecionado, {@link Optional#empty()} caso não exista nenhum
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso a tabela permita a seleção de múltiplos elementos ou caso seu modelo não permita o acesso a um elemento a
	 *             partir de seu índice
	 */
	@SuppressWarnings("unchecked")
	public static <E> Optional<E> getSelectedElement(JTable table) {
		Objects.requireNonNull(table, "Tabela nula");
		if (table.getSelectionModel().getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
			throw new IllegalArgumentException("A tabela permite a seleção de múltiplos elementos");
		}

		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {
			int modelIndex = table.convertRowIndexToModel(selectedRow);
			TableModel model = table.getModel();
			if (model instanceof ObjectTableModel) {
				return modelIndex != -1 ? (Optional<E>) Optional.of(((ObjectTableModel<?>) model).getElement(modelIndex))
						: Optional.empty();
			} else {
				throw new IllegalArgumentException(
						"Não é possível obter um elemento do modelo da tabela a partir de seu índice: " + modelIndex);
			}
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Executa uma tarefa e aguarda (bloqueia a <em>thread</em> atual) até que ela finalize.
	 * 
	 * @param doRun
	 *            tarefa a ser executada
	 * @throws ViewException
	 *             caso ocorra algum erro durante a execução da tarefa ou ocorra alguma interrupção enquanto aguardava a execução
	 *             da tarefa
	 * @see #invokeLater(Runnable)
	 * @see #invokeAndWait(Callable)
	 */
	public static void invokeAndWait(Runnable doRun) throws ViewException {
		try {
			SwingUtilities.invokeAndWait(doRun);
		} catch (InvocationTargetException | InterruptedException exception) {
			throw handle(exception);
		}
	}

	/**
	 * Executa uma tarefa de forma assíncrona.
	 * 
	 * @param doRun
	 *            tarefa a ser executada
	 * @see #invokeAndWait(Runnable)
	 * @see #invokeLater(Callable)
	 */
	public static void invokeLater(Runnable doRun) {
		SwingUtilities.invokeLater(doRun);
	}

	/**
	 * Executa uma tarefa que retorna valor e aguarda (bloqueia a <em>thread</em> atual) até que ela finalize.
	 * 
	 * @param doRun
	 *            tarefa a ser executada
	 * @return o resultado da execução da tarefa
	 * @throws ViewException
	 *             caso ocorra algum erro durante a execução da tarefa ou ocorra alguma interrupção enquanto aguardava a execução
	 *             da tarefa
	 */
	public static <T> T invokeAndWait(Callable<T> doRun) throws ViewException {
		try {
			return invokeLater(doRun).get();
		} catch (ExecutionException | InterruptedException exception) {
			throw handle(exception);
		}
	}

	private static RuntimeException handle(Exception exception) {
		Throwable cause = exception.getCause();
		if (cause instanceof RuntimeException) {
			return (RuntimeException) cause;
		} else {
			return new ViewException(exception);
		}
	}

	/**
	 * Executa uma tarefa que retorna valor de forma assíncrona.
	 * 
	 * @param doRun
	 *            tarefa a ser executada
	 * @return objeto de promessa de execução da tarefa
	 * @see #invokeAndWait(Callable)
	 * @see #invokeLater(Runnable)
	 */
	public static <T> FutureTask<T> invokeLater(Callable<T> doRun) {
		FutureTask<T> task = new FutureTask<>(doRun);
		invokeLater(task);
		return task;
	}
}
