package br.com.colbert.mychart.aplicacao.topmusical;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.SwingWorker;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.Presenter;
import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.principal.PainelTelaPrincipal;
import br.com.colbert.mychart.ui.topmusical.TopMusicalPanel;

/**
 * <em>Presenter</em> de {@link TopMusical}.
 * 
 * @author Thiago Colbert
 * @since 29/01/2015
 */
public class TopMusicalPresenter implements Presenter, Serializable {

	private final class TopAtualCarregadoWorkerDoneListener extends WorkerDoneAdapter {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void doneWithSuccess(SwingWorker<?, ?> worker) {
			Optional<TopMusical> resultado = ((AbstractWorker<Optional<TopMusical>, ?>) worker).getResult();
			logger.debug("Top atual: {}", resultado);

			if (!resultado.isPresent()) {
				logger.debug("Nenhum top ainda salvo. Criando um novo.");
				messagesView
						.adicionarMensagemSucesso("É a sua primeira vez aqui, portanto é necessário informar alguns dados do seu primeiro top musical.");
				primeiroTopMusicalPresenter.start();
				resultado = primeiroTopMusicalPresenter.getTopMusical();
				logger.debug("Top criado: {}", resultado);
			}

			setTopAtual(resultado);
		}
	}

	private static final long serialVersionUID = 7945528859962384121L;

	@Inject
	private Logger logger;
	@Inject
	private AppController appController;

	private TopMusical topAtual;

	@Inject
	@PainelTelaPrincipal
	private TopMusicalPanel view;
	@Inject
	private MessagesView messagesView;
	@Inject
	private PrimeiroTopMusicalPresenter primeiroTopMusicalPresenter;

	@Inject
	private Instance<CarregarTopAtualWorker> carregarTopAtualWorker;
	@Inject
	private Instance<SalvarTopMusicalWorker> salvarTopMusicalWorker;

	@PostConstruct
	@Override
	public void doBinding() {
		logger.trace("Definindo bindings");
		appController.bindPresenter(view, this);
	}

	@Override
	public void start() {
		logger.debug("Iniciando");
		carregarTopAtual();
	}

	private void carregarTopAtual() {
		CarregarTopAtualWorker worker = carregarTopAtualWorker.get();
		worker.execute();
		worker.addWorkerDoneListener(new TopAtualCarregadoWorkerDoneListener());
	}

	public void topAnterior() {
		logger.trace("Mudando para o top anterior");
		setTopAtual(topAtual.getAnterior());
	}

	public void proximoTop() {
		logger.trace("Mudando para o próximo top");
		setTopAtual(topAtual.getProximo());
	}

	private void setTopAtual(Optional<TopMusical> optionalTop) {
		optionalTop.ifPresent(top -> {
			this.topAtual = top;
			appController.bindModel(view, top);
			view.getAnteriorButton().setEnabled(top.getAnterior().isPresent());
			view.getProximoButton().setEnabled(top.getProximo().isPresent());
		});
	}

	public void estreia() {
		// TODO
		// topMusical.entrada(event.getNumeroPosicao(), event.getCancao());
		// messagesView.adicionarMensagemSucesso(cancaoFormatter.format(event.getCancao()) + " estreou!");
	}

	public void retorno() {
		// TODO
	}

	public void saida() {
		Optional<Posicao> optionalPosicao = view.getPosicaoSelecionada();
		optionalPosicao.ifPresent(posicao -> {
			logger.info("Saída: {}", posicao);
			topAtual.saida(posicao.getNumero());
		});
	}

	public void salvar() {
		logger.info("Salvando o top atual");

		SalvarTopMusicalWorker worker = salvarTopMusicalWorker.get();
		worker.setTopMusical(topAtual);
		worker.execute();
		worker.addWorkerDoneListener(new WorkerDoneAdapter() {

			private static final long serialVersionUID = -2453040686995578166L;

			@Override
			public void doneWithSuccess(SwingWorker<?, ?> worker) {
				messagesView.adicionarMensagemSucesso("Salvo com sucesso!");
			}
			
			@Override
			public void doneWithError(SwingWorker<?, ?> worker, Throwable throwable) {
				messagesView.adicionarMensagemErro("Erro ao salvar top", throwable);
			}
		});
	}
}
