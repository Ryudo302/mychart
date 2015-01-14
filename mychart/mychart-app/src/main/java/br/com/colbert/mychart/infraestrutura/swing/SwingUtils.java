package br.com.colbert.mychart.infraestrutura.swing;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

import javax.swing.SwingUtilities;

import br.com.colbert.mychart.infraestrutura.exception.ViewException;

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
