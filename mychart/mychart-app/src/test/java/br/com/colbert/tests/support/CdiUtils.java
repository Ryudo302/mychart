package br.com.colbert.tests.support;

import java.lang.annotation.Annotation;

import org.jboss.weld.environment.se.*;
import org.jboss.weld.exceptions.WeldException;

/**
 * Classe utilitária para operações do CDI.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
public final class CdiUtils {

	private static Weld weld;
	private static WeldContainer container;

	public CdiUtils() {
	}

	/**
	 * Inicializa o contêiner CDI.
	 * 
	 * @throws IllegalStateException
	 *             caso o contêiner ainda já tenha sido inicializado
	 */
	public static void init() {
		if (isInicialized()) {
			throw new IllegalStateException("Contexto já inicializado!");
		}

		weld = new Weld();
		container = weld.initialize();
	}

	/**
	 * Verifica se o contêiner CDI está inicializado.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	public static boolean isInicialized() {
		return weld != null;
	}

	/**
	 * Desliga o contêiner CDI.
	 * 
	 * @throws IllegalStateException
	 *             caso o contêiner ainda não tenha sido inicializado
	 */
	public static void shutdown() {
		assertIsInitialized();
		weld.shutdown();
		weld = null;
		container = null;
	}

	/**
	 * Obtém um bean a partir de sua classe.
	 * 
	 * @param beanClass
	 *            a classe do bean desejado
	 * @return o bean do tipo informado
	 * @param qualifiers
	 *            qualificadores do bean
	 * @throws IllegalStateException
	 *             caso o contêiner ainda não tenha sido inicializado
	 * @throws WeldException
	 *             caso ocorra algum erro ao recuperar o bean
	 */
	public static <T> T getBean(Class<T> beanClass, Annotation... qualifiers) {
		assertIsInitialized();
		return container.instance().select(beanClass, qualifiers).get();
	}

	private static void assertIsInitialized() {
		if (!isInicialized()) {
			throw new IllegalStateException("Ainda não inicializado!");
		}
	}
}
