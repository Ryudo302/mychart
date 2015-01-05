package br.com.colbert.mychart.aplicacao.topmusical;

import java.io.Serializable;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.dominio.topmusical.repository.TopMusicalRepository;
import br.com.colbert.mychart.infraestrutura.eventos.topmusical.*;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.infraestrutura.formatter.CancaoFormatter;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.topmusical.TopMusicalView;

/**
 * Controlador de ações referentes a tops musicais.
 * 
 * @author Thiago Colbert
 * @since 09/12/2014
 */
@ApplicationScoped
public class TopMusicalController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;

	@Inject
	private TopMusicalView topMusicalView;
	@Inject
	private MessagesView messagesView;

	@Inject
	private TopMusicalRepository repositorio;

	@Inject
	private TopMusicalFactory topMusicalFactory;
	@Inject
	private CancaoFormatter cancaoFormatter;

	public void carregarTopAtual(@Observes TopMusicalView topMusicalView) {
		TopMusical topMusical;
		Optional<TopMusical> topAtual = null;

		try {
			topAtual = repositorio.consultarAtual();
		} catch (RepositoryException exception) {
			logger.error("Erro ao carregar o top atual", exception);
			messagesView.adicionarMensagemErro("Erro ao carregar o top atual", exception.getLocalizedMessage());
			return;
		}

		if (topAtual.isPresent()) {
			logger.debug("Top atual: {}", topAtual);
			topMusical = topAtual.get();
		} else {
			logger.debug("Nenhum top ainda salvo. Criando um novo.");
			messagesView
					.adicionarMensagemSucesso("É a sua primeira vez aqui, portanto é necessário informar alguns dados do seu primeiro top musical.");
			// TODO Criar tela de criação do primeiro top musical
			// topMusical = topMusicalFactory.novo(dataInicial, cancoes);
			topMusical = null;
		}

		topMusicalView.setTopMusical(topMusical);
	}

	public void estreia(@Observes @MudancaTopMusical(TipoMudancaTopMusical.ESTREIA) MudancaTopMusicalEvent event) {
		logger.info("Estréia: {}", event);

		TopMusical topMusical = topMusicalView.getTopMusical();
		try {
			topMusical.entrada(event.getNumeroPosicao(), event.getCancao());
			messagesView.adicionarMensagemSucesso(cancaoFormatter.format(event.getCancao()) + " estreou!");
		} catch (ElementoJaExistenteException exception) {
			logger.error("Erro ao processar estreia: " + event, exception);
			messagesView.adicionarMensagemErro("Erro ao processar estreia", exception.getLocalizedMessage());
		}
	}

	public void retorno(@Observes @MudancaTopMusical(TipoMudancaTopMusical.RETORNO) MudancaTopMusicalEvent event) {
		estreia(event);
	}

	public void saida(@Observes @MudancaTopMusical(TipoMudancaTopMusical.SAIDA) MudancaTopMusicalEvent event) {
		logger.info("Saída: {}", event);
		topMusicalView.getTopMusical().saida(event.getNumeroPosicao());
		messagesView.adicionarMensagemSucesso("Saída!");
	}
}
