package br.com.colbert.mychart.infraestrutura.jpa.helper;

/**
 * Uma regra de validação para a execução de alguma tarefa.
 *
 * @author Thiago Colbert
 * @since 04/03/2015
 */
public interface RegraValidacao<T> {

	/**
	 * Faz a validação de um objeto, verificando se ele atende às regras definidas.
	 *
	 * @param objeto
	 *            a ser validado
	 * @return <code>true</code>/<code>false</code>
	 */
	boolean isValido(T objeto);

	/**
	 * Obtém a mensagem de erro definida para quando um objeto não atende às regras de validação.
	 *
	 * @return a mensagem de erro de validação
	 */
	String getMensagemErro();
}
