package br.com.colbert.mychart.aplicacao;

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
import br.com.colbert.mychart.ui.MessagesView;
import br.com.colbert.mychart.ui.artista.ArtistaView;

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
		logger.info("Consultando com base em exemplo: {}", exemplo);

		try {
			view.setArtistas(consultaArtistaService.consultarPor(exemplo));
		} catch (Exception exception) {
			logger.error("Erro ao consultar artistas a partir do exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar artistas: " + exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void adicionarNova(@Observes @OperacaoCrud(TipoOperacaoCrud.INSERCAO) Artista artista) {
		logger.info("Adicionando: {}", artista);

		try {
			validar(artista);
			repositorio.adicionar(artista);
			messagesView.adicionarMensagemSucesso("Artista incluída com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (ElementoJaExistenteException exception) {
			logger.debug("Elemento já existente", exception);
			messagesView.adicionarMensagemAlerta("A artista informada já existe! Utilizar atualização ao invés de inclusão.");
		} catch (RepositoryException exception) {
			logger.error("Erro ao adicionar artista: " + artista, exception);
			messagesView.adicionarMensagemErro("Erro ao adicionar artista: " + exception.getLocalizedMessage());
		}
	}

	@Transactional
	public void removerExistente(@Observes @OperacaoCrud(TipoOperacaoCrud.REMOCAO) Artista artista) {
		logger.info("Removendo: {}", artista);

		try {
			validar(artista);
			repositorio.remover(artista);
			messagesView.adicionarMensagemSucesso("Artista removida com sucesso!");
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (RepositoryException exception) {
			logger.error("Erro ao remover artista: " + artista, exception);
			messagesView.adicionarMensagemErro("Erro ao remover artista: " + exception.getLocalizedMessage());
		}
	}

	private void validar(Artista artista) throws ValidacaoException {
		logger.debug("Validando: {}", artista);
		Objects.requireNonNull(artista, "A artista não foi informada");

		for (Validador<Artista> validador : validadores) {
			validador.validar(artista);
		}
	}
}
