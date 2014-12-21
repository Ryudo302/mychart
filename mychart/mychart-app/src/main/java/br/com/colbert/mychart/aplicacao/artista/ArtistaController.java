package br.com.colbert.mychart.aplicacao.artista;

import java.io.Serializable;
import java.util.Objects;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepositoryLocal;
import br.com.colbert.mychart.dominio.artista.service.ConsultaArtistaService;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.ui.artista.ArtistaView;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;

/**
 * Controlador de ações referentes a artistas.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaController implements Serializable {

	private static final long serialVersionUID = 2909647942422289099L;

	@Inject
	private Logger logger;

	@Inject
	private ArtistaView view;
	@Inject
	private MessagesView messagesView;

	@Inject
	private ConsultaArtistaService consultaArtistaService;
	@Inject
	private ArtistaRepositoryLocal repositorio;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	public void consultarExistentes(@Observes @OperacaoCrud(TipoOperacaoCrud.CONSULTA) Artista exemplo) {
		logger.info("Consultando artistas com base em exemplo: {}", exemplo);

		try {
			view.setArtistas(consultaArtistaService.consultarPor(exemplo));
		} catch (Exception exception) {
			logger.error("Erro ao consultar artistas a partir do exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar artistas", exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void adicionarNova(@Observes @OperacaoCrud(TipoOperacaoCrud.INSERCAO) Artista artista) {
		logger.info("Adicionando: {}", artista);

		try {
			validar(artista);
			repositorio.adicionar(artista);
			messagesView.adicionarMensagemSucesso("Artista incluído com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (ElementoJaExistenteException exception) {
			logger.debug("Artista já existente", exception);
			messagesView.adicionarMensagemAlerta("Já existe um artista com o nome informado.");
		} catch (RepositoryException exception) {
			logger.error("Erro ao adicionar artista: " + artista, exception);
			messagesView.adicionarMensagemErro("Erro ao adicionar artista", exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void removerExistente(@Observes @OperacaoCrud(TipoOperacaoCrud.REMOCAO) Artista artista) {
		logger.info("Removendo: {}", artista);

		try {
			validar(artista);
			repositorio.remover(artista);
			messagesView.adicionarMensagemSucesso("Artista removido com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (RepositoryException exception) {
			logger.error("Erro ao remover artista: " + artista, exception);
			messagesView.adicionarMensagemErro("Erro ao remover artista", exception.getLocalizedMessage());
		}
	}

	private void validar(Artista artista) throws ValidacaoException {
		logger.debug("Validando: {}", artista);
		Objects.requireNonNull(artista, "O artista não foi informado");

		for (Validador<Artista> validador : validadores) {
			validador.validar(artista);
		}
	}
}
