package br.com.colbert.mychart.dominio.cancao.service;

import java.util.*;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.cancao.repository.*;
import br.com.colbert.mychart.infraestrutura.eventos.entidade.ModoConsulta;
import br.com.colbert.mychart.infraestrutura.exception.*;

/**
 * Serviço contendo regras de negócio de operações envolvendo {@link Cancao}.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ConsultaCancaoService {

	@Inject
	private Logger logger;

	private final CancaoRepositoryLocal repositorioLocal;
	private final CancaoRepositoryRemoto repositorioRemoto;

	/**
	 * Cria um novo serviço com os repositórios informados.
	 *
	 * @param repositorioLocal
	 *            repositório utilizado nas operações locais
	 * @param repositorioRemoto
	 *            repositório utilizado nas operações remotas
	 */
	@Inject
	public ConsultaCancaoService(CancaoRepositoryLocal repositorioLocal, CancaoRepositoryRemoto repositorioRemoto) {
		this.repositorioLocal = Objects.requireNonNull(repositorioLocal, "O repositório local é obrigatório");
		this.repositorioRemoto = Objects.requireNonNull(repositorioRemoto, "O repositório remoto é obrigatório");
	}

	/**
	 * Faz uma consulta por canções a partir de uma canção de exemplo.
	 *
	 * @param exemplo
	 *            a ser utilizado na consulta
	 * @param modoConsulta
	 *            indica o modo de consulta
	 * @return as canções encontrados (pode ser uma lista vazia)
	 * @throws NullPointerException
	 *             caso o exemplo seja <code>null</code>
	 * @throws ServiceException
	 *             caso ocorra algum erro durante a operação
	 */
	public Set<Cancao> consultarPor(Cancao exemplo, ModoConsulta modoConsulta) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		Set<Cancao> resultadoTotal = new HashSet<>();

		logger.debug("Consultando por canções no repositório local com base em exemplo: {}", exemplo);
		resultadoTotal.addAll(consultarRepositorioLocalPor(exemplo));

		if (modoConsulta == ModoConsulta.TODOS) {
			logger.debug("Consultando por canções no repositório remoto com base em exemplo: {}", exemplo);
			resultadoTotal.addAll(consultarRepositorioRemotoPor(exemplo));
		}

		return resultadoTotal;
	}

	private Collection<Cancao> consultarRepositorioLocalPor(Cancao exemplo) throws ServiceException {
		try {
			return repositorioLocal.consultarPor(exemplo);
		} catch (RepositoryException exception) {
			throw new ServiceException("Erro ao consultar repositório local", exception);
		}
	}

	private Collection<? extends Cancao> consultarRepositorioRemotoPor(Cancao exemplo) throws ServiceException {
		String titulo = exemplo.getTitulo();

		if (StringUtils.isNotBlank(titulo)) {
			try {
				return repositorioRemoto.consultarPor(titulo);
			} catch (RepositoryException exception) {
				throw new ServiceException("Erro ao consultar repositório remoto", exception);
			}
		} else {
			logger.debug("Nenhum título informado - a consulta não será realizada");
			return CollectionUtils.emptyCollection();
		}
	}
}
