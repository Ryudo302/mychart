package br.com.colbert.mychart.aplicacao.artista;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.*;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.jpa.ArtistaJpaRepository;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.infraestrutura.swing.worker.WorkerWaitListener;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes da classe {@link ConsultaArtistasWorker}.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@AdditionalClasses({ ArtistaValidador.class, ArtistaJpaRepository.class, LastFmWs.class })
public class ConsultaArtistasWorkerIT extends AbstractDbUnitTest {

	@Inject
	private ConsultaArtistasWorker worker;

	@Produces
	@ProducesAlternative
	@Mock
	private WorkerWaitListener workerWaitListener;

	@Override
	protected String getDataSetFileName() {
		return "artistas-dataset.xml";
	}

	@Test
	public void testConsultarExistentes() {
		Artista exemplo = new Artista("rihanna", TipoArtista.FEMININO_SOLO);

		worker.setExemplo(exemplo);
		worker.execute();
		Collection<Artista> artistas = worker.getResult();

		assertThat(artistas, is(notNullValue(Collection.class)));
		assertThat(artistas.size(), is(equalTo(1)));
	}
}
