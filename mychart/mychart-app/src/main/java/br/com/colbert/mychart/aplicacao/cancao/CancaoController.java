package br.com.colbert.mychart.aplicacao.cancao;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.base.aplicacao.validacao.Validador;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepository;
import br.com.colbert.mychart.dominio.cancao.service.CancaoWs;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.eventos.entidade.*;
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

	public void consultarExistentes(@Observes @OperacaoCrud(TipoOperacaoCrud.CONSULTA) ConsultaEntidadeEvent evento) {
		Cancao exemplo = evento.getEntidade();

		logger.info("Consultando canções com base em exemplo: {}", exemplo);
		Collection<Cancao> cancoes;

		try {
			if (evento.getModoConsulta() == ModoConsulta.TODOS) {
				cancoes = new ArrayList<>();

				logger.debug("Consultando no repositório local");
				cancoes.addAll(repositorio.consultarPor(exemplo));

				logger.debug("Consultando na web");
				cancoes.addAll(cancaoWs.consultarPor(exemplo));
			} else {
				logger.debug("Consultando no repositório local");
				cancoes = repositorio.consultarPor(exemplo);
			}

			view.setCancoes(cancoes);
		} catch (Exception exception) {
			logger.error("Erro ao consultar canções a partir de exemplo: " + exemplo, exception);
			messagesView.adicionarMensagemErro("Erro ao consultar canções", exception.getLocalizedMessage());
		}
	}
}
