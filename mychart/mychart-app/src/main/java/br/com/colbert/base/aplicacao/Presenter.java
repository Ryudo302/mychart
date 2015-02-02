package br.com.colbert.base.aplicacao;

/**
 * Um <em>presenter</em> do modelo MVP.
 * 
 * @author Thiago Colbert
 * @since 02/02/2015
 */
public interface Presenter {

	/**
	 * Faz o <em>binding</em> entre a <em>view</em>, o <em>model</em> e o próprio <em>presenter</em>.
	 */
	void doBinding();

	/**
	 * Inicia o <em>presenter</em>.
	 */
	void start();
}
