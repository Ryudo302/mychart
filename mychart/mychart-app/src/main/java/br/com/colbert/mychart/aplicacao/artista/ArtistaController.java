package br.com.colbert.mychart.aplicacao.artista;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.mychart.ui.comum.messages.*;

/**
 * Controlador de ações referentes a artistas.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@ApplicationScoped
public class ArtistaController implements Serializable {

	private static final long serialVersionUID = 2909647942422289099L;

	@Inject
	private Logger logger;

	@Inject
	private MessagesView messagesView;

	@Inject
	private ArtistaRepository repositorio;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	/**
	 * Remove um artista do repositório.
	 * 
	 * @param artista
	 *            a ser removido
	 */
	@Transactional
	public void removerExistente(@Observes @OperacaoCrud(TipoOperacaoCrud.REMOCAO) Artista artista) {
		Objects.requireNonNull(artista, "O artista não foi informado");
		if (!artista.getPersistente()) {
			messagesView.adicionarMensagemAlerta("O artista não pode ser removido porque ainda não foi salvo.");
		} else {
			logger.info("Removendo: {}", artista);

			if (messagesView
					.exibirConfirmacao(MessageFormat.format("Deseja realmente excluir o artista {0}?", artista.getNome())) == RespostaConfirmacao.SIM) {
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
		}
	}

	private void validar(Artista artista) throws ValidacaoException {
		logger.debug("Validando: {}", artista);
		for (Validador<Artista> validador : validadores) {
			validador.validar(artista);
		}
	}
}
