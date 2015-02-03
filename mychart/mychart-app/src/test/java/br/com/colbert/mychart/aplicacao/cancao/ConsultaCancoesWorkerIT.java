package br.com.colbert.mychart.aplicacao.cancao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.*;
import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.jpa.CancaoJpaRepository;
import br.com.colbert.mychart.infraestrutura.lastfm.LastFmWs;
import br.com.colbert.mychart.infraestrutura.swing.worker.WorkerWaitListener;
import br.com.colbert.tests.support.AbstractDbUnitTest;

/**
 * Testes da classe {@link ConsultarCancoesWorker}.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@AdditionalClasses({ CancaoValidador.class, CancaoJpaRepository.class, LastFmWs.class })
public class ConsultaCancoesWorkerIT extends AbstractDbUnitTest {

	@Inject
	private ConsultarCancoesWorker worker;

	@Produces
	@ProducesAlternative
	@Mock
	private WorkerWaitListener workerWaitListener;

	@Override
	protected String getDataSetFileName() {
		return "cancoes-dataset.xml";
	}

	@Test
	public void testConsultarExistentes() {
		Cancao exemplo = new Cancao("Rehab", new Artista("Rihanna", TipoArtista.FEMININO_SOLO));

		worker.setExemplo(exemplo);
		worker.execute();
		Collection<Cancao> cancoes = worker.getResult();

		assertThat(cancoes, is(notNullValue(Collection.class)));
		assertThat(cancoes.size(), is(equalTo(1)));
	}
}
