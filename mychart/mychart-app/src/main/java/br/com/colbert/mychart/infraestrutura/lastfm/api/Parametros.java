package br.com.colbert.mychart.infraestrutura.lastfm.api;

/**
 * Parâmetros utilizados pelos métodos dos WebServices da LastFM.
 * 
 * @author Thiago Colbert
 * @since 22/12/2014
 */
public enum Parametros {

	METODO("method"),

	API_KEY("api_key"),

	FORMATO("format"),

	/**
	 * Parâmetro referente a nome de um artista.
	 */
	ARTISTA("artist"),

	/**
	 * Parâmetro referente a título de uma canção.
	 */
	CANCAO("track");

	private String nome;

	private Parametros(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return getNome();
	}
}
