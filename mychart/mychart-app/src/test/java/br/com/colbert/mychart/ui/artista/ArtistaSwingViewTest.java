package br.com.colbert.mychart.ui.artista;

import java.util.Arrays;

import javax.swing.JFrame;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.swing.artista.ArtistaSwingView;
import br.com.colbert.tests.support.CdiUtils;

/**
 * 
 * @author Thiago Colbert
 * @since 11/12/2014
 */
public class ArtistaSwingViewTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testInWeld();
	}

	private static void testInWeld() {
		Artista artista1 = new Artista("Fulano 1", TipoArtista.FEMININO_SOLO);
		Artista artista2 = new Artista("Fulano 2", TipoArtista.GRUPO_OU_BANDA);
		Artista artista3 = new Artista("Fulano 3", TipoArtista.MASCULINO_SOLO);

		try {
			CdiUtils.init();
			ArtistaSwingView artistaSwingView = CdiUtils.getBean(ArtistaSwingView.class);

			artistaSwingView.setArtistaAtual(artista1);
			artistaSwingView.setArtistas(Arrays.asList(artista1, artista2, artista3));

			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.getContentPane().add(artistaSwingView.getContainer());

			frame.pack();
			frame.setVisible(true);
		} finally {
			CdiUtils.shutdown();
		}
	}

	private static void testStandalone() {
		Artista artista1 = new Artista("Fulano 1", TipoArtista.FEMININO_SOLO);
		Artista artista2 = new Artista("Fulano 2", TipoArtista.GRUPO_OU_BANDA);
		Artista artista3 = new Artista("Fulano 3", TipoArtista.MASCULINO_SOLO);

		ArtistaSwingView artistaSwingView = new ArtistaSwingView();
		artistaSwingView.setArtistaAtual(artista1);
		artistaSwingView.setArtistas(Arrays.asList(artista1, artista2, artista3));

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(artistaSwingView.getContainer());

		frame.pack();
		frame.setVisible(true);
	}
}
