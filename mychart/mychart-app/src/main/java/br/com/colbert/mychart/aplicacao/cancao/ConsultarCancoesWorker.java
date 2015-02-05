package br.com.colbert.mychart.aplicacao.cancao;

import java.util.*;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepository;
import br.com.colbert.mychart.dominio.cancao.service.CancaoWs;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;

/**
 * Executor de consulta de canções.
 * 
 * @author Thiago Colbert
 * @since 03/02/2015
 */
public class ConsultarCancoesWorker extends AbstractWorker<Collection<Cancao>, Void> {

	private static final class CancoesPorTituloComparator implements Comparator<Cancao> {

		@Override
		public int compare(Cancao cancao1, Cancao cancao2) {
			return new CompareToBuilder().append(cancao1.getTitulo(), cancao2.getTitulo()).toComparison();
		}
	}

	@Inject
	private Logger logger;

	@Inject
	private CancaoRepository repositorio;
	@Inject
	private CancaoWs cancaoWs;

	private Cancao exemplo;

	@Override
	protected Collection<Cancao> doInBackground() throws Exception {
		verificarSeExemploFoiDefinido();

		logger.info("Consultando canções com base em exemplo: {}", exemplo);
		List<Cancao> cancoes = new ArrayList<>();
		List<Cancao> cancoesUniqueList = SetUniqueList.setUniqueList(cancoes);

		logger.debug("Consultando no repositório local");
		cancoesUniqueList.addAll(repositorio.consultarPor(exemplo));

		logger.debug("Consultando na web");
		cancoesUniqueList.addAll(cancaoWs.consultarPor(exemplo));

		logger.debug("Ordenando canções");
		cancoes.sort(new CancoesPorTituloComparator());

		return cancoesUniqueList;
	}

	private void verificarSeExemploFoiDefinido() {
		if (Objects.isNull(exemplo)) {
			throw new IllegalStateException("A canção de exemplo não foi informada");
		}
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao consultar canções a partir do exemplo: {}", exemplo, exception);
			fireError(exception);
		}
	}

	public Cancao getExemplo() {
		return exemplo;
	}

	public void setExemplo(Cancao exemplo) {
		this.exemplo = exemplo;
	}
}
