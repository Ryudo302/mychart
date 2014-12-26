package br.com.colbert.mychart.infraestrutura.eventos.entidade;

import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Evento representando uma requisição de consulta de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 26/12/2014
 */
public class ConsultaCancaoEvent extends ConsultaEntidadeEvent<Cancao> {

	private static final long serialVersionUID = 8105973385934156801L;

	public ConsultaCancaoEvent(Cancao cancao, ModoConsulta modoConsulta) {
		super(cancao, modoConsulta);
	}
}
