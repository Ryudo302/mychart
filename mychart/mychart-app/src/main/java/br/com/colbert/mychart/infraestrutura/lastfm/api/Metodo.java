package br.com.colbert.mychart.infraestrutura.lastfm.api;

/**
 * Enumeração dos métodos providos pelos WebServices da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public enum Metodo {

	/**
	 * Consulta de artistas.
	 */
	CONSULTA_ARTISTA("artist.search"),

	/**
	 * Consulta de canções.
	 */
	CONSULTA_CANCAO("track.search");

	private String nome;

	Metodo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}
