package br.com.colbert.mychart.infraestrutura.lastfm;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes de integração da {@link ArtistaLastFmWs}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaLastFmWsIT extends AbstractTest {

	@Inject
	private ArtistaLastFmWs artistaWs;

	@Test
	public void testConsultarPor() throws ServiceException {
		Collection<Artista> artistas = artistaWs.consultarPor(new Artista("rihanna", TipoArtista.DESCONHECIDO));

		assertThat(artistas, is(notNullValue(Collection.class)));
		assertThat(artistas.size(), is(not(equalTo(0))));

		System.out.println(artistas);
	}
}
