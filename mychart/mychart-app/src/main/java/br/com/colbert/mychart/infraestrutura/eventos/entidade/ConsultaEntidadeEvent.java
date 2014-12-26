package br.com.colbert.mychart.infraestrutura.eventos.entidade;

import java.io.Serializable;

import br.com.colbert.base.dominio.Entidade;

/**
 * Evento representando uma requisição de consulta de {@link Entidade}.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
public abstract class ConsultaEntidadeEvent<T extends Entidade<?>> implements Serializable {

	private static final long serialVersionUID = -167414477846752667L;

	private final T entidade;
	private final ModoConsulta modoConsulta;

	/**
	 * Cria um novo evento definindo a entidade a ser utilizado como exemplo na consulta, bem como o modo que a consulta deve ser
	 * feita.
	 * 
	 * @param entidade
	 * @param modoConsulta
	 */
	public ConsultaEntidadeEvent(T entidade, ModoConsulta modoConsulta) {
		this.entidade = entidade;
		this.modoConsulta = modoConsulta;
	}

	public T getEntidade() {
		return entidade;
	}

	public ModoConsulta getModoConsulta() {
		return modoConsulta;
	}
}
