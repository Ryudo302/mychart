package br.com.colbert.mychart.infraestrutura.jpa;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.cancao.*;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepository;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapper;

/**
 * Uma implementação de {@link CancaoRepository} que utiliza o JPA.
 * 
 * @author Thiago Colbert
 * @since 15/12/2014
 */
public class CancaoJpaRepository implements CancaoRepository {

	@Inject
	private Logger logger;

	@Inject
	private Instance<EntityManager> entityManager;

	private EntityManager getEntityManager() {
		return entityManager.get();
	}

	@Override
	@SuppressWarnings("unchecked")
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao consultar canções pelo título: '{0}'")
	public Collection<Cancao> consultarPorTituloEArtistaExatos(Cancao exemplo) throws RepositoryException {
		Objects.requireNonNull(exemplo, "O exemplo é obrigatório");

		EntityManager entityManager = getEntityManager();
		Query query = entityManager.createNamedQuery("ArtistaCancao.findCancaoByTituloEArtistasExatos");
		query.setParameter("titulo", exemplo.getTitulo());
		query.setParameter("nomeArtista", exemplo.getArtistaPrincipal().get().getNome());

		List<ArtistaCancao> artistasCancoes = query.getResultList();
		logger.debug("Resultados de canções com o mesmo título e artista(s): {}", artistasCancoes);

		return artistasCancoes.stream().map(artistasCancao -> artistasCancao.getCancao()).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao consultar canções")
	public Collection<Cancao> consultarPor(Cancao exemplo) throws RepositoryException {
		return getEntityManager()
				.unwrap(Session.class)
				.createCriteria(Cancao.class)
				.add(Example.create(Objects.requireNonNull(exemplo, "O exemplo é obrigatório")).enableLike(MatchMode.ANYWHERE)
						.excludeZeroes().ignoreCase()).list();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao adicionar canção: {0}")
	public void adicionar(Cancao cancao) throws ElementoJaExistenteException, RepositoryException {
		Objects.requireNonNull(cancao, "A canção a ser adicionada é obrigatória");

		logger.debug("Verificando se já existe uma canção com o mesmo título e do(s) mesmo(s) artista(s): {}", cancao);
		if (!consultarPorTituloEArtistaExatos(cancao).isEmpty()) {
			throw new ElementoJaExistenteException(cancao);
		}

		try {
			logger.debug("Persistindo cancao");
			getEntityManager().persist(cancao);
		} catch (EntityExistsException exception) {
			throw new ElementoJaExistenteException(cancao);
		}
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao alterar canção: {0}")
	public void alterar(Cancao cancao) throws ElementoNaoExistenteException, RepositoryException {
		Objects.requireNonNull(cancao, "A canção a ser alterada é obrigatória");

		if (cancao.getId() != null) {
			logger.debug("Alterando canção");
			getEntityManager().merge(cancao);
		} else {
			throw new ElementoNaoExistenteException("Canção", cancao);
		}
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao remover canção: {0}")
	public boolean remover(Cancao cancao) throws RepositoryException {
		Objects.requireNonNull(cancao, "A canção a ser removida é obrigatória");

		logger.debug("Removendo canção");
		EntityManager entityManager = getEntityManager();
		try {
			logger.debug("Verificando se a canção existe no repositório: {}", cancao);
			Cancao cancaoRemover = entityManager.getReference(Cancao.class, cancao.getId());
			entityManager.remove(cancaoRemover);
			return true;
		} catch (EntityNotFoundException exception) {
			logger.debug("A canção já não existia no repositório");
			return false;
		}
	}
}
