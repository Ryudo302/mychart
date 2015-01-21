package br.com.colbert.mychart.aplicacao.artista;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

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
import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;
import br.com.colbert.mychart.ui.artista.ArtistaPanel;
import br.com.colbert.mychart.ui.comum.messages.*;

/**
 * <em>Presenter</em> de {@link Artista}.
 * 
 * @author Thiago Colbert
 * @since 16/01/2015
 */
@ApplicationScoped
public class ArtistaPresenter implements Serializable {

	private static final long serialVersionUID = 8111564698178618449L;

	private final class ArtistaWorkerDoneListener extends WorkerDoneAdapter {

		private static final long serialVersionUID = -200308075599872055L;

		private String mensagemSucesso;
		private String mensagemErro;
		private boolean limparTela;

		public ArtistaWorkerDoneListener(String mensagemSucesso, String mensagemErro, boolean limparTelaQuandoSucesso) {
			this.mensagemSucesso = mensagemSucesso;
			this.mensagemErro = mensagemErro;
			this.limparTela = limparTelaQuandoSucesso;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void doneWithSuccess(SwingWorker<?, ?> worker) {
			messagesView.adicionarMensagemSucesso(formatarMensagem(mensagemSucesso, worker));
			if (limparTela) {
				view.limparTela();
			}

			Object result = ((AbstractWorker<?, ?>) worker).getResult();
			if (result != null && result instanceof Collection) {
				view.setArtistas((Collection<Artista>) result);
			}
		}

		private String formatarMensagem(String mensagem, SwingWorker<?, ?> worker) {
			if (mensagem.indexOf('{') != -1) {
				String metodo = mensagem.substring(mensagem.indexOf('{') + 1, mensagem.indexOf('}'));
				Object result = ((AbstractWorker<?, ?>) worker).getResult();
				return mensagem.replace('{' + metodo + '}', invokeMethod(result, metodo));
			} else {
				return mensagem;
			}
		}

		private String invokeMethod(Object result, String metodo) {
			try {
				return result.getClass().getMethod(metodo).invoke(result).toString();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException exception) {
				throw new IllegalArgumentException("Erro ao invocar método " + metodo + " do objeto " + result, exception);
			}
		}

		@Override
		public void doneWithError(SwingWorker<?, ?> worker, String errorMessage) {
			messagesView.adicionarMensagemErro(mensagemErro, errorMessage);
		}
	}

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
		appController.bind(view, Artista.ARTISTA_NULL, this);
	}

	public void start() {
		logger.debug("Iniciando");
	}

	public void consultarArtistas() {
		logger.info("Consultando artistas");
		ConsultaArtistasWorker worker = consultaArtistasWorker.get();
		String nomeArtista = view.getNomeArtista();
		if (StringUtils.isBlank(nomeArtista)) {
			messagesView.adicionarMensagemAlerta("Informe um nome a ser consultado.");
		} else {
			worker.setExemplo(criarArtista(null, nomeArtista, view.getTipoArtista()));
			worker.execute();

			worker.addWorkerDoneListener(new ArtistaWorkerDoneListener("Foi(ram) encontrado(s) {size} artista(s)",
					"Erro ao consultar artistas", false));
		}
	}

	public void salvarArtista() {
		logger.info("Salvando artista");
		SalvarArtistaWorker worker = salvarArtistaWorker.get();
		worker.setArtista(criarArtista(view.getIdArtista(), view.getNomeArtista(), view.getTipoArtista()));
		worker.execute();
		worker.addWorkerDoneListener(new ArtistaWorkerDoneListener("Artista salvo com sucesso!", "Erro ao salvar artista", true));
	}

	public void removerArtista() {
		logger.info("Removendo artista");
		if (messagesView.exibirConfirmacao("Deseja realmente excluir o artista selecionado?") == RespostaConfirmacao.SIM) {
			RemoverArtistaWorker worker = removerArtistaWorker.get();
			worker.setIdArtista(view.getIdArtista());
			worker.execute();
			worker.addWorkerDoneListener(new ArtistaWorkerDoneListener("Artista removido com sucesso!",
					"Erro ao remover artista", true));
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
			view.setEstadoAtual(EstadoTelaCrud.INCLUSAO_OU_ALTERACAO);
		});
	}

	private Artista criarArtista(String id, String nome, TipoArtista tipo) {
		return new Artista(id, nome, tipo);
	}
}
