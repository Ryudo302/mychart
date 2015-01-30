package br.com.colbert.mychart.aplicacao.artista;

import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.lang3.StringUtils;
import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.base.ui.EstadoTelaCrud;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.TipoArtista;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;
import br.com.colbert.mychart.infraestrutura.swing.worker.DefinirConteudoTabelaWorkerListener;
import br.com.colbert.mychart.infraestrutura.swing.worker.LimparTelaWorkerListener;
import br.com.colbert.mychart.infraestrutura.swing.worker.MensagensWorkerListener;
import br.com.colbert.mychart.infraestrutura.swing.worker.WorkerDoneAdapter;
import br.com.colbert.mychart.ui.artista.ArtistaPanel;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.comum.messages.RespostaConfirmacao;

/**
 * <em>Presenter</em> de {@link Artista}.
 * 
 * @author Thiago Colbert
 * @since 16/01/2015
 */
@ApplicationScoped
public class ArtistaPresenter implements Serializable {

	private static final long serialVersionUID = 8111564698178618449L;

	@Inject
	private Logger logger;
	@Inject
	private AppController appController;

	@Inject
	private ArtistaPanel view;
	@Inject
	private MessagesView messagesView;

	@Inject
	private Instance<ConsultaArtistasWorker> consultaArtistasWorker;
	@Inject
	private Instance<SalvarArtistaWorker> salvarArtistaWorker;
	@Inject
	private Instance<RemoverArtistaWorker> removerArtistaWorker;

	@PostConstruct
	protected void doBinding() {
		logger.trace("Definindo bindings");
		appController.bind(view, Artista.ARTISTA_NULL.clone(), this);
	}

	public void start() {
		logger.debug("Iniciando");
	}

	public void consultarArtistas() {
		logger.info("Consultando artistas");
		String nomeArtista = view.getNomeArtista();
		if (StringUtils.isBlank(nomeArtista)) {
			messagesView.adicionarMensagemAlerta("Informe um nome a ser consultado.");
		} else {
			ConsultaArtistasWorker worker = consultaArtistasWorker.get();
			worker.setExemplo(criarArtista(null, nomeArtista, view.getTipoArtista()));
			worker.execute();
			worker.addWorkerDoneListener(new DefinirConteudoTabelaWorkerListener<>(view));
			worker.addWorkerDoneListener(new MensagensWorkerListener(messagesView, "Foi(ram) encontrado(s) {size} artista(s)",
					"Erro ao consultar artistas"));
		}
	}

	public void salvarArtista() {
		logger.info("Salvando artista");
		SalvarArtistaWorker worker = salvarArtistaWorker.get();
		worker.setArtista(criarArtista(view.getIdArtista(), view.getNomeArtista(), view.getTipoArtista()));
		worker.execute();
		worker.addWorkerDoneListener(new WorkerDoneAdapter() {

			private static final long serialVersionUID = 8235417074768298229L;

			@Override
			@SuppressWarnings("unchecked")
			public void doneWithSuccess(SwingWorker<?, ?> worker) {
				view.atualizarArtista(((AbstractWorker<Artista, ?>) worker).getResult());
			}
		});

		worker.addWorkerDoneListener(new MensagensWorkerListener(messagesView, "Artista salvo com sucesso!",
				"Erro ao salvar artista"));
		worker.addWorkerDoneListener(new LimparTelaWorkerListener(view));
	}

	public void removerArtista() {
		logger.info("Removendo artista");
		if (messagesView.exibirConfirmacao("Deseja realmente excluir o artista selecionado?") == RespostaConfirmacao.SIM) {
			RemoverArtistaWorker worker = removerArtistaWorker.get();
			worker.setIdArtista(view.getIdArtista());
			worker.execute();
			worker.addWorkerDoneListener(new WorkerDoneAdapter() {

				private static final long serialVersionUID = 8235417074768298229L;

				@Override
				public void doneWithSuccess(SwingWorker<?, ?> worker) {
					Artista artista = Artista.ARTISTA_NULL.clone();
					artista.setId(view.getIdArtista());
					artista.setNome(view.getNomeArtista());
					view.atualizarArtista(artista);
				}
			});

			worker.addWorkerDoneListener(new MensagensWorkerListener(messagesView, "Artista removido com sucesso!",
					"Erro ao remover artista"));
			worker.addWorkerDoneListener(new LimparTelaWorkerListener(view));
		}
	}

	public void teclaPressionada(KeyEvent evento) {
		int keyCode = evento.getKeyCode();
		logger.trace("Tecla pressionada: " + keyCode);
		if (keyCode == KeyEvent.VK_ESCAPE) {
			view.limparTela();
		} else if (keyCode == KeyEvent.VK_DELETE) {
			view.getArtistaSelecionado().ifPresent(artista -> removerArtista());
		}
	}

	public void artistaSelecionado(ListSelectionEvent evento) {
		view.getArtistaSelecionado().ifPresent(artista -> {
			logger.trace("Artista selecionado: " + artista);
			appController.bindModel(view, artista);
			appController.refreshView(view);
			view.setEstadoAtual(EstadoTelaCrud.INCLUSAO_OU_ALTERACAO);
		});
	}

	public void limparCampos() {
		logger.trace("Limpando campos");
		view.limparTela();
	}

	private Artista criarArtista(String id, String nome, TipoArtista tipo) {
		return new Artista(id, nome, tipo);
	}
}
