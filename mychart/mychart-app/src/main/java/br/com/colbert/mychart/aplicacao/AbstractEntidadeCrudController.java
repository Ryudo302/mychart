package br.com.colbert.mychart.aplicacao;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.base.dominio.*;
import br.com.colbert.base.ui.EntidadeView;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.ui.MessagesView;

/**
 * Implementação-base para todos os controllers de telas que manipulam entidades com operações CRUD.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 *
 * @param <T>
 *            o tipo de entidade
 */
public abstract class AbstractEntidadeCrudController<T extends Entidade<ID>, ID extends Serializable> {

	@Inject
	protected Logger logger;

	protected EntidadeView<T> view;

	@Inject
	protected MessagesView messagesView;

	@Inject
	@Any
	private Instance<Validador<T>> validadores;

	private CrudRepository<T, ID> repositorio;

	public AbstractEntidadeCrudController(EntidadeView<T> view, CrudRepository<T, ID> repositorio) {
		this.view = view;
		this.repositorio = repositorio;
	}

	public void consultarExistentes(@Observes @OperacaoCrud(TipoOperacaoCrud.CONSULTA) T exemplo) {
		logger.info("Consultando com base em exemplo: {}", exemplo);

		try {
			view.setEntidades(doConsultar(exemplo));
		} catch (Exception exception) {
			logger.error("Erro ao consultar entidades a partir do exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar entidades: " + exception.getLocalizedMessage());
		}
	}

	protected Collection<T> doConsultar(T exemplo) throws Exception {
		return repositorio.consultarPor(exemplo);
	}

	@Transactional
	public void adicionarNova(@Observes @OperacaoCrud(TipoOperacaoCrud.INSERCAO) T entidade) {
		logger.info("Adicionando: {}", entidade);

		try {
			validar(entidade);
			repositorio.adicionar(entidade);
			messagesView.adicionarMensagemSucesso("Entidade incluída com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (ElementoJaExistenteException exception) {
			logger.debug("Elemento já existente", exception);
			messagesView.adicionarMensagemAlerta("A entidade informada já existe! Utilizar atualização ao invés de inclusão.");
		} catch (RepositoryException exception) {
			logger.error("Erro ao adicionar entidade: " + entidade, exception);
			messagesView.adicionarMensagemErro("Erro ao adicionar entidade: " + exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void atualizarExistente(@Observes @OperacaoCrud(TipoOperacaoCrud.ATUALIZACAO) T entidade) {
		logger.info("Atualizando: {}", entidade);

		try {
			validar(entidade);
			repositorio.atualizar(entidade);
			messagesView.adicionarMensagemSucesso("Entidade atualizada com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (ElementoNaoExistenteException exception) {
			logger.debug("Elemento inexistente", exception);
			messagesView.adicionarMensagemAlerta("A entidade informada não existe! Utilizar inclusão ao invés de atualização.");
		} catch (RepositoryException exception) {
			logger.error("Erro ao atualizar entidade: " + entidade, exception);
			messagesView.adicionarMensagemErro("Erro ao atualizar entidade: " + exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void removerExistente(@Observes @OperacaoCrud(TipoOperacaoCrud.REMOCAO) T entidade) {
		logger.info("Removendo: {}", entidade);

		try {
			validar(entidade);
			repositorio.remover(entidade);
			messagesView.adicionarMensagemSucesso("Entidade removida com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (RepositoryException exception) {
			logger.error("Erro ao remover entidade: " + entidade, exception);
			messagesView.adicionarMensagemErro("Erro ao remover entidade: " + exception.getLocalizedMessage());
		}
	}

	private void validar(T entidade) throws ValidacaoException {
		logger.debug("Validando: {}", entidade);
		Objects.requireNonNull(entidade, "A entidade não foi informada");

		for (Validador<T> validador : validadores) {
			validador.validar(entidade);
		}
	}
}