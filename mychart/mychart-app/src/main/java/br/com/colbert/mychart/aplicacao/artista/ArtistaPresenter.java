package br.com.colbert.mychart.aplicacao.artista;

import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.event.ListSelectionEvent;

import org.mvp4j.AppController;
import org.mvp4j.adapter.MVPBinding;

import br.com.colbert.base.ui.EstadoTelaCrud;
import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.swing.worker.WorkerStateAdapter;
import br.com.colbert.mychart.ui.artista.ArtistaPanel;

/**
 * <em>Presenter</em> de {@link Artista}.
 * 
 * @author Thiago Colbert
 * @since 16/01/2015
 */
@ApplicationScoped
public class ArtistaPresenter implements Serializable {

	private static final long serialVersionUID = 8111564698178618449L;

	private final class LimparTelaWorkerListener extends WorkerStateAdapter {

		private static final long serialVersionUID = -200308075599872055L;

		@Override
		public void done() {
			view.limparTela();
		}
	}

	@Inject
	private AppController appController;

	@Inject
	private ArtistaPanel view;

	@Inject
	private Instance<ConsultaArtistasWorker> consultaArtistasWorker;
	@Inject
	private Instance<SalvarArtistaWorker> salvarArtistaWorker;
	@Inject
	private Instance<RemoverArtistaWorker> removerArtistaWorker;

	private MVPBinding binding;

	@PostConstruct
	protected void doBinding() {
		binding = appController.bind(view, Artista.ARTISTA_NULL, this);
	}

	public void start() {

	}

	public void consultarArtistas() {
		ConsultaArtistasWorker worker = consultaArtistasWorker.get();
		worker.setExemplo(criarArtista(view.getNomeArtista(), view.getTipoArtista()));
		worker.execute();
	}

	public void salvarArtista() {
		SalvarArtistaWorker worker = salvarArtistaWorker.get();
		worker.setArtista(criarArtista(view.getNomeArtista(), view.getTipoArtista()));
		worker.execute();
		worker.addWorkerStateListener(new LimparTelaWorkerListener());
	}

	public void removerArtista() {
		RemoverArtistaWorker worker = removerArtistaWorker.get();
		worker.setIdArtista(view.getIdArtista());
		worker.execute();
		worker.addWorkerStateListener(new LimparTelaWorkerListener());
	}

	public void teclaPressionada(KeyEvent evento) {
		int keyCode = evento.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			view.limparTela();
		} else if (keyCode == KeyEvent.VK_DELETE) {
			view.getArtistaSelecionado().ifPresent(artista -> removerArtista());
		}
	}

	public void artistaSelecionado(ListSelectionEvent evento) {
		view.getArtistaSelecionado().ifPresent(artista -> {
			binding.setModel(artista);
			view.setEstadoAtual(EstadoTelaCrud.INCLUSAO_OU_ALTERACAO);
		});
	}

	private Artista criarArtista(String nome, TipoArtista tipo) {
		return new Artista(nome, tipo);
	}
}
