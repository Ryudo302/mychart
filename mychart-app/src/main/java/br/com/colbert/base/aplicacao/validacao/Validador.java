package br.com.colbert.base.aplicacao.validacao;

/**
 * Classe que faz a validação dos dados de um objeto.
 * 
 * @author Thiago Colbert
 * @since 12/12/2014
 *
 * @param <T>
 *            tipo de objeto validado
 */
public interface Validador<T> {

	/**
	 * Faz a validação de um objeto.
	 * 
	 * @param objeto
	 *            a ser validado
	 * @throws ValidacaoException
	 *             caso ocorram erros de validação
	 */
	void validar(T objeto) throws ValidacaoException;
}
