package br.com.colbert.mychart.aplicacao.artista;

import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.mychart.infraestrutura.swing.worker.AbstractWorker;
import br.com.colbert.mychart.ui.comum.messages.RespostaConfirmacao;

/**
 * Executor de remoção de artistas.
 * 
 * @author Thiago Colbert
 * @since 16/01/2015
 */
public class RemoverArtistaWorker extends AbstractWorker<Boolean, Void> {

	@Inject
	private Logger logger;

	@Inject
	private ArtistaRepository repositorio;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	private String idArtista;

	@Override
	@Transactional
	protected Boolean doInBackground() throws Exception {
		Boolean retorno = Boolean.FALSE;

		if (StringUtils.isBlank(idArtista)) {
			throw new IllegalStateException("ID do artista não definido");
		} else {
			logger.info("Removendo artista com ID: {}", idArtista);

			if (messagesView.exibirConfirmacao("Deseja realmente excluir o artista selecionado?") == RespostaConfirmacao.SIM) {
				try {
					repositorio.remover(idArtista);
					retorno = Boolean.TRUE;
				} catch (RepositoryException exception) {
					logger.error("Erro ao remover artista pelo ID: {}", idArtista, exception);
					messagesView.adicionarMensagemErro("Erro ao remover artista", exception.getLocalizedMessage());
				}
			}
		}

		return retorno;
	}

	@Override
	protected void done() {
		try {
			if (BooleanUtils.isTrue(get())) {
				messagesView.adicionarMensagemSucesso("Artista removido com sucesso!");
			}
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao salvar artista", exception);
			messagesView.adicionarMensagemErro("Erro ao salvar artista", exception.getLocalizedMessage());
		}
	}

	public String getIdArtista() {
		return idArtista;
	}

	public void setIdArtista(String idArtista) {
		this.idArtista = idArtista;
	}
}
