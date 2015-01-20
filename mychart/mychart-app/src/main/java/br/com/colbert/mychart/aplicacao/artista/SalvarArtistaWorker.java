package br.com.colbert.mychart.aplicacao.artista;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.mychart.infraestrutura.swing.worker.*;

/**
 * Executor de salvamento de artistas.
 * 
 * @author Thiago Colbert
 * @since 14/01/2015
 */
public class SalvarArtistaWorker extends AbstractWorker<Boolean, Void> {

	@Inject
	private Logger logger;

	@Inject
	private ArtistaRepository repositorio;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	private Artista artista;

	@Override
	@Transactional
	protected Boolean doInBackground() throws Exception {
		verificarSeArtistaFoiDefinido();

		Boolean retorno = Boolean.FALSE;
		logger.info("Salvando: {}", artista);

		try {
			WorkflowEntidade.novo(artista, repositorio).validar(validadores).incluirOuAlterar(artista);
			retorno = Boolean.TRUE;
		} catch (ValidacaoException exception) {
			logger.debug("Erros de validação", exception);
			messagesView.adicionarMensagemAlerta(exception.getLocalizedMessage());
		} catch (RepositoryException exception) {
			logger.error("Erro ao adicionar artista: " + artista, exception);
			messagesView.adicionarMensagemErro("Erro ao adicionar artista", exception.getLocalizedMessage());
		}

		return retorno;
	}

	private void verificarSeArtistaFoiDefinido() {
		if (Objects.isNull(artista)) {
			throw new IllegalStateException("O artista não foi informado");
		}
	}

	@Override
	protected void done() {
		try {
			if (BooleanUtils.isTrue(get())) {
				messagesView.adicionarMensagemSucesso("Artista salvo com sucesso!");
			}
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao salvar artista", exception);
			messagesView.adicionarMensagemErro("Erro ao salvar artista", exception.getLocalizedMessage());
		}
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}
}
