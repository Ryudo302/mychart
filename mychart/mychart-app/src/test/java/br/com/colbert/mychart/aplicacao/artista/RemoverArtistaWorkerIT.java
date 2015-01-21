package br.com.colbert.mychart.aplicacao.artista;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.swing.SwingWorker;

import org.jglue.cdiunit.*;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.infraestrutura.jpa.ArtistaJpaRepository;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes de integração da {@link RemoverArtistaWorker}.
 * 
 * @author Thiago Colbert
 * @since 21/01/2015
 */
@AdditionalClasses({ ArtistaJpaRepository.class })
public class RemoverArtistaWorkerIT extends AbstractDbUnitTest {

	@Inject
	private RemoverArtistaWorker worker;

	@Produces
	@ProducesAlternative
	@Mock
	private WorkerWaitListener workerWaitListener;

	@Override
	protected String getDataSetFileName() {
		return "artistas-dataset.xml";
	}

	@Test
	public void testRemoverArtistaExistente() {
		worker.setIdArtista("69989475-2971-49aa-8c53-5d74af88b8be");
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

		Boolean removido = worker.getResult();

		assertThat("O artista não foi removido", removido, is(equalTo(Boolean.TRUE)));
	}

	@Test
	public void testRemoverArtistaInexistente() {
		worker.setIdArtista("xxx");
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

		Boolean removido = worker.getResult();

		assertThat("Nenhum artista deveria ser removido", removido, is(equalTo(Boolean.FALSE)));
	}
}
