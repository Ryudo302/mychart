package br.com.colbert.mychart.infraestrutura.lastfm;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.inject.Inject;

import org.junit.*;

import de.umass.lastfm.Caller;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes de integração da {@link LastFmWs}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class LastFmWsIT extends AbstractTest {

	@Inject
	private Caller caller;
	@Inject
	private LastFmWs ws;

	@Before
	public void setUp() {
		caller.setCache(null);
	}

	@Test
	public void testConsultarArtista() throws ServiceException {
		Collection<Artista> artistas = ws.consultarPor(new Artista("rihanna", TipoArtista.DESCONHECIDO));

		System.out.println(artistas);

		assertThat(artistas, is(notNullValue(Collection.class)));
		assertThat(artistas.size(), is(not(equalTo(0))));
	}
}
