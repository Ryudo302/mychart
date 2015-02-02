package br.com.colbert.base.ui;

import java.util.Collection;

/**
 * Uma tela que possui um formulário com um ou mais campos de entrada de dados do usuário.
 * 
 * @author Thiago Colbert
 * @since 23/01/2015
 * 
 * @param <T>
 *            o tipo de objeto representado na tela
 */
public interface FormView<T> extends View {

	/**
	 * Limpa todos os campos da tela.
	 */
	void limparTela();

	/**
	 * Define o conteúdo da tabela.
	 * 
	 * @param elementos
	 *            a serem exibidos na tabela
	 */
	void setConteudoTabela(Collection<T> elementos);
}