package br.com.colbert.mychart.aplicacao.cancao;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepository;
import br.com.colbert.mychart.dominio.cancao.service.CancaoWs;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.eventos.entidade.*;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.ui.cancao.CancaoView;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;

/**
 * Controlador de ações referentes a canções.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
public class CancaoController implements Serializable {

	private static final long serialVersionUID = -444876850735933938L;

	@Inject
	private Logger logger;

	@Inject
	private CancaoView view;
	@Inject
	private MessagesView messagesView;

	@Inject
	private CancaoRepository repositorio;
	@Inject
	private CancaoWs cancaoWs;

	@Inject
	@Any
	private Instance<Validador<Cancao>> validadores;

	public void consultarExistentes(@Observes @OperacaoCrud(TipoOperacaoCrud.CONSULTA) ConsultaCancaoEvent evento) {
		Cancao exemplo = evento.getEntidade();

		logger.info("Consultando canções com base em exemplo: {}", exemplo);
		Collection<Cancao> cancoes = new ArrayList<>();

		try {
			logger.debug("Consultando no repositório local");
			cancoes.addAll(repositorio.consultarPor(exemplo));

			if (evento.getModoConsulta() == ModoConsulta.TODOS) {
				logger.debug("Consultando na web");
				cancoes.addAll(cancaoWs.consultarPor(exemplo));
			}

			view.setCancoes(cancoes);
		} catch (RepositoryException | ServiceException exception) {
			logger.error("Erro ao consultar canções a partir de exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar canções", exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void adicionarNova(@Observes @OperacaoCrud(TipoOperacaoCrud.INSERCAO) Cancao cancao) {
		logger.info("Adicionando: {}", cancao);

		try {
			validar(cancao);
			repositorio.adicionar(cancao);
			messagesView.adicionarMensagemSucesso("Canção incluída com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (ElementoJaExistenteException exception) {
			logger.debug("Canção já existente", exception);
			messagesView.adicionarMensagemAlerta("Já existe uma canção com o título e artista(s) informados.");
		} catch (RepositoryException exception) {
			logger.error("Erro ao adicionar canção: " + cancao, exception);
			messagesView.adicionarMensagemErro("Erro ao adicionar artista", exception.getLocalizedMessage());
		}
	}

	private void validar(Cancao cancao) throws ValidacaoException {
		logger.debug("Validando: {}", cancao);
		Objects.requireNonNull(cancao, "A canção não foi informada");

		for (Validador<Cancao> validador : validadores) {
			validador.validar(cancao);
		}
	}
}
