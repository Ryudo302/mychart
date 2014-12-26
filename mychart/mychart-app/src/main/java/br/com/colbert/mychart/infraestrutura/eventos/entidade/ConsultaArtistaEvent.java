package br.com.colbert.mychart.infraestrutura.eventos.entidade;

import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Evento representando uma requisição de consulta de {@link Artista}.
 * 
 * @author Thiago Colbert
 * @since 26/12/2014
 */
public class ConsultaArtistaEvent extends ConsultaEntidadeEvent<Artista> {

	private static final long serialVersionUID = 8105973385934156801L;
	
	public ConsultaArtistaEvent(Artista artista, ModoConsulta modoConsulta) {
		super(artista, modoConsulta);
	}
}
