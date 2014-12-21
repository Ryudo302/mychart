package br.com.colbert.mychart.infraestrutura.eventos.artista;

import java.io.Serializable;

import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Evento representando uma requisição de consulta de {@link Artista}.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
public class ConsultaArtistaEvent implements Serializable {

	private static final long serialVersionUID = -167414477846752667L;

	private final Artista artista;
	private final ModoConsulta modoConsulta;

	/**
	 * Cria um novo evento definindo o artista a ser utilizado como exemplo na consulta, bem como o modo que a consulta deve ser
	 * feita.
	 * 
	 * @param artista
	 * @param modoConsulta
	 */
	public ConsultaArtistaEvent(Artista artista, ModoConsulta modoConsulta) {
		this.artista = artista;
		this.modoConsulta = modoConsulta;
	}

	public Artista getArtista() {
		return artista;
	}

	public ModoConsulta getModoConsulta() {
		return modoConsulta;
	}
}
