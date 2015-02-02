package br.com.colbert.mychart.aplicacao.topmusical;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.SwingWorker;

import org.mvp4j.AppController;
import org.mvp4j.adapter.MVPBinding;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.Presenter;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;
import br.com.colbert.mychart.ui.comum.CausaSaidaDeView;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.principal.PainelTelaPrincipal;
import br.com.colbert.mychart.ui.topmusical.*;

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
			Optional<TopMusical> resultado = (Optional<TopMusical>) ((AbstractWorker<?, ?>) worker).getResult();
			logger.debug("Top atual: {}", resultado);

			if (!resultado.isPresent()) {
				logger.debug("Nenhum top ainda salvo. Criando um novo.");
				messagesView
						.adicionarMensagemSucesso("É a sua primeira vez aqui, portanto é necessário informar alguns dados do seu primeiro top musical.");
				String causaSaida = primeiroTopMusicalDialog.show();
				if (causaSaida == CausaSaidaDeView.CONFIRMACAO) {
					List<Cancao> cancoes = primeiroTopMusicalDialog.getCancoes();
					Integer quantidadePosicoes = topMusicalFactory.getConfig().getQuantidadePosicoes();
					if (cancoes.size() == quantidadePosicoes) {
						resultado = Optional.of(topMusicalFactory.novo(primeiroTopMusicalDialog.getDataInicial(), cancoes));
					} else {
						messagesView.adicionarMensagemAlerta("Devem ser adicionadas " + quantidadePosicoes + " canções!");
					}
				} else {
					resultado = Optional.empty();
				}
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
	private PrimeiroTopMusicalDialog primeiroTopMusicalDialog;

	@Inject
	private TopMusicalFactory topMusicalFactory;

	@Inject
	private Instance<CarregarTopAtualWorker> carregarTopAtualWorker;

	private MVPBinding binding;

	@PostConstruct
	@Override
	public void doBinding() {
		logger.trace("Definindo bindings");
		binding = appController.bindPresenter(view, this);
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
			binding.setModel(top);
			appController.refreshView(view);
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
	}
}
