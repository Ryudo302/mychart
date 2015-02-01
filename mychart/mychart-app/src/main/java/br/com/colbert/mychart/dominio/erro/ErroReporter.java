package br.com.colbert.mychart.dominio.erro;

import br.com.colbert.mychart.ui.comum.messages.Erro;

/**
 * Responsável por enviar notificações de erros que ocorreram na aplicação para os desenvolvedores.
 * 
 * @author Thiago Colbert
 * @since 01/02/2015
 */
public interface ErroReporter {

	/**
	 * Reporta um erro.
	 * 
	 * @param erro
	 *            a ser reportado
	 * @throws ErroReporterException
	 *             caso ocorra algum erro durante a execução
	 */
	void reportar(Erro erro);
}
