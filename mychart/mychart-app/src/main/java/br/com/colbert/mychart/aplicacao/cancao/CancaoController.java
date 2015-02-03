package br.com.colbert.mychart.aplicacao.cancao;

import java.io.Serializable;
import java.util.Objects;

import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
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
	private MessagesView messagesView;

	@Inject
	private CancaoRepository repositorio;

	@Inject
	@Any
	private Instance<Validador<Cancao>> validadores;

	@Transactional
	public void adicionarNova(Cancao cancao) {
		logger.info("Adicionando: {}", cancao);

		try {
			validar(cancao);
			repositorio.incluirOuAlterar(cancao);
			messagesView.adicionarMensagemSucesso("Canção incluída com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (RepositoryException exception) {
			logger.error("Erro ao adicionar canção: " + cancao, exception);
			messagesView.adicionarMensagemErro("Erro ao adicionar artista", exception);
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
