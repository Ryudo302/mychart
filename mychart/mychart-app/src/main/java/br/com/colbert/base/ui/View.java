package br.com.colbert.base.ui;

import java.awt.Container;

/**
 * Uma <em>view</em> do modelo MVP.
 * 
 * @author Thiago Colbert
 * @since 02/02/2015
 */
public interface View {

	/**
	 * Obtém o nome da <em>view</em>.
	 * 
	 * @return o nome
	 */
	default String getName() {
		return this.getClass().getName();
	};

	/**
	 * Obtém uma instância de {@link Container} que representa o contêiner AWT/Swing utilizado para representar a <em>view</em>.
	 * 
	 * @return o contêiner AWT/Swing
	 */
	Container getContainer();
}
