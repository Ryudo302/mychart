package br.com.colbert.mychart.aplicacao.artista;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.swing.SwingWorker;

import org.jglue.cdiunit.*;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.jpa.ArtistaJpaRepository;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes de integração da {@link SalvarArtistaWorker}.
 * 
 * @author Thiago Colbert
 * @since 21/01/2015
 */
@AdditionalClasses({ ArtistaValidador.class, ArtistaJpaRepository.class })
public class SalvarArtistaWorkerIT extends AbstractDbUnitTest {

	@Inject
	private SalvarArtistaWorker worker;

	@Produces
	@ProducesAlternative
	@Mock
	private WorkerWaitListener workerWaitListener;

	@Override
	protected String getDataSetFileName() {
		return "artistas-dataset.xml";
	}

	@Test
	public void testSalvarArtistaExistente() {
		Artista artista = new Artista("69989475-2971-49aa-8c53-5d74af88b8be", "Rihanna2", TipoArtista.FEMININO_SOLO);

		worker.setArtista(artista);
		worker.execute();

		worker.addWorkerDoneListener(new WorkerDoneListener() {

			@Override
			public void doneWithSuccess(SwingWorker<?, ?> worker) {

			}

			@Override
			public void doneWithError(SwingWorker<?, ?> worker, String errorMessage) {
				fail(errorMessage);
			}
		});

		artista = worker.getResult();

		assertThat("O artista não foi salvo", artista.getPersistente(), is(true));
	}

	@Test
	public void testAdicionarNovoComArtistaNovo() {
		Artista artista = new Artista("XXX", "Fulano", TipoArtista.MASCULINO_SOLO);

		worker.setArtista(artista);
		worker.execute();

		worker.addWorkerDoneListener(new WorkerDoneListener() {

			@Override
			public void doneWithSuccess(SwingWorker<?, ?> worker) {

			}

			@Override
			public void doneWithError(SwingWorker<?, ?> worker, String errorMessage) {
				fail(errorMessage);
			}
		});

		artista = worker.getResult();

		assertThat("O artista não foi salvo", artista.getPersistente(), is(true));
	}

	@Test
	public void testAdicionarNovoComArtistaInvalido() {
		Artista artista = new Artista(null, null);

		worker.setArtista(artista);
		worker.execute();

		worker.addWorkerDoneListener(new WorkerDoneListener() {

			@Override
			public void doneWithSuccess(SwingWorker<?, ?> worker) {
				fail("Deveria falhar");
			}

			@Override
			public void doneWithError(SwingWorker<?, ?> worker, String errorMessage) {
				assertThat(errorMessage, containsString("Validacao"));
			}
		});

		try {
			worker.getResult();
		} catch (IllegalStateException exception) {
			assertThat(exception, is(notNullValue(IllegalStateException.class)));
		}

		assertThat("O artista não deveria ser salvo", artista.getPersistente(), is(false));
	}
}
