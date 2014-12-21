package br.com.colbert.mychart.dominio.artista.service;

import java.util.*;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.*;
import br.com.colbert.mychart.infraestrutura.eventos.artista.ModoConsulta;
import br.com.colbert.mychart.infraestrutura.exception.*;

/**
 * Serviço contendo regras de negócio de operações envolvendo {@link Artista}s.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ConsultaArtistaService {

	@Inject
	private Logger logger;

	private final ArtistaRepositoryLocal repositorioLocal;
	private final ArtistaRepositoryRemoto repositorioRemoto;

	/**
	 * Cria um novo serviço com os repositórios informados.
	 *
	 * @param repositorioLocal
	 *            repositório utilizado nas operações locais
	 * @param repositorioRemoto
	 *            repositório utilizado nas operações remotas
	 */
	@Inject
	public ConsultaArtistaService(ArtistaRepositoryLocal repositorioLocal, ArtistaRepositoryRemoto repositorioRemoto) {
		this.repositorioLocal = Objects.requireNonNull(repositorioLocal, "O repositório local é obrigatório");
		this.repositorioRemoto = Objects.requireNonNull(repositorioRemoto, "O repositório remoto é obrigatório");
	}

	/**
	 * Faz uma consulta por artistas a partir de um artista de exemplo.
	 *
	 * @param exemplo
	 *            a ser utilizado na consulta
	 * @param modoConsulta
	 *            indica o modo de consulta
	 * @return os artistas encontrados (pode ser uma lista vazia)
	 * @throws NullPointerException
	 *             caso o exemplo seja <code>null</code>
	 * @throws ServiceException
	 *             caso ocorra algum erro durante a operação
	 */
	public Set<Artista> consultarPor(Artista exemplo, ModoConsulta modoConsulta) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		Set<Artista> resultadoTotal = new HashSet<>();

		logger.debug("Consultando por artistas no repositório local com base em exemplo: {}", exemplo);
		resultadoTotal.addAll(consultarRepositorioLocalPor(exemplo));

		if (modoConsulta == ModoConsulta.TODOS) {
			logger.debug("Consultando por artistas no repositório remoto com base em exemplo: {}", exemplo);
			resultadoTotal.addAll(consultarRepositorioRemotoPor(exemplo));
		}

		return resultadoTotal;
	}

	private Collection<Artista> consultarRepositorioLocalPor(Artista exemplo) throws ServiceException {
		try {
			return repositorioLocal.consultarPor(exemplo);
		} catch (RepositoryException exception) {
			throw new ServiceException("Erro ao consultar repositório local", exception);
		}
	}

	private Collection<? extends Artista> consultarRepositorioRemotoPor(Artista exemplo) throws ServiceException {
		String nome = exemplo.getNome();

		if (StringUtils.isNotBlank(nome)) {
			try {
				return repositorioRemoto.consultarPor(nome);
			} catch (RepositoryException exception) {
				throw new ServiceException("Erro ao consultar repositório remoto", exception);
			}
		} else {
			logger.debug("Nenhum nome informado - a consulta não será realizada");
			return CollectionUtils.emptyCollection();
		}
	}
}
