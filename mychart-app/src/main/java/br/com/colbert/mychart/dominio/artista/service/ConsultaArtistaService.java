package br.com.colbert.mychart.dominio.artista.service;

import java.util.*;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.*;
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
	 * @return os artistas encontrados (pode ser uma lista vazia)
	 * @throws NullPointerException
	 *             caso o exemplo seja <code>null</code>
	 * @throws ServiceException
	 *             caso ocorra algum erro durante a operação
	 */
	public Set<Artista> consultarPor(Artista exemplo) throws ServiceException {
		Objects.requireNonNull(exemplo, "O exemplo a ser utilizado na consulta é obrigatório");

		Set<Artista> resultadoTotal = new HashSet<>();

		logger.info("Consultando por artistas com nome '{}' no repositório local", exemplo);
		try {
			resultadoTotal.addAll(repositorioLocal.consultarPor(exemplo));
		} catch (RepositoryException exception) {
			throw new ServiceException("Erro ao consultar repositório local", exception);
		}

		logger.info("Consultando por artistas com nome '{}' no repositório remoto", exemplo);
		try {
			resultadoTotal.addAll(repositorioRemoto.consultarPor(exemplo.getNome()));
		} catch (RepositoryException exception) {
			throw new ServiceException("Erro ao consultar repositório remoto", exception);
		}

		return resultadoTotal;
	}
}
