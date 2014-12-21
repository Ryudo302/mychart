package br.com.colbert.mychart.ui.artista;

import java.util.Collection;

import br.com.colbert.base.ui.View;
import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Uma view de {@link Artista}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public interface ArtistaView extends View {

	/**
	 * Define o artista atualmente sendo exibido em detalhes na visão.
	 * 
	 * @param artista
	 */
	void setArtistaAtual(Artista artista);

	/**
	 * Define os artistas a serem listadas na visão.
	 * 
	 * @param artistas
	 */
	void setArtistas(Collection<Artista> artistas);
}
