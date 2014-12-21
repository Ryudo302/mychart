package br.com.colbert.mychart.aplicacao.cancao;

import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepositoryLocal;
import br.com.colbert.mychart.dominio.cancao.service.ConsultaCancaoService;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.eventos.entidade.ConsultaEntidadeEvent;
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
	private ConsultaCancaoService consultaService;
	@Inject
	private CancaoRepositoryLocal repositorioLocal;

	@Inject
	@Any
	private Instance<Validador<Artista>> validadores;

	public void consultarExistentes(@Observes @OperacaoCrud(TipoOperacaoCrud.CONSULTA) ConsultaEntidadeEvent evento) {
		Cancao exemplo = evento.getEntidade();

		logger.info("Consultando artistas com base em exemplo: {}", exemplo);

		try {
			view.setCancoes(consultaService.consultarPor(exemplo, evento.getModoConsulta()));
		} catch (Exception exception) {
			logger.error("Erro ao consultar artistas a partir do exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar artistas", exception.getLocalizedMessage());
		}
	}
}
