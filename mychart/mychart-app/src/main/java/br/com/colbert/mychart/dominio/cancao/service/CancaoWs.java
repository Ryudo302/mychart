package br.com.colbert.mychart.dominio.cancao.service;

import java.util.Set;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;

/**
 * Serviço com operações na web envolvendo {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public interface CancaoWs {

	/**
	 * Faz uma consulta por canções utilizando como parâmetro uma canção de exemplo.
	 * 
	 * @param exemplo
	 *            a ser utilizado na consulta
	 * @return as canções encontradas (pode ser vazio)
	 * @throws NullPointerException
	 *             caso o exemplo seja <code>null</code>
	 * @throws ServiceException
	 *             caso ocorra algum erro na consulta
	 */
	public abstract Set<Cancao> consultarPor(Cancao exemplo) throws ServiceException;

}