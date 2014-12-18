package br.com.colbert.mychart.dominio.artista.repository;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapper;

/**
 * Uma implementação de {@link ArtistaRepositoryLocal} que utiliza o JPA.
 * 
 * @author Thiago Colbert
 * @since 15/12/2014
 */
public class ArtistaJpaRepository implements ArtistaRepositoryLocal {

	@Inject
	private Logger logger;

	@Inject
	private EntityManager entityManager;

	@Override
	@ExceptionWrapper(para = RepositoryException.class, mensagem = "Erro ao consultar artistas pelo nome: '{0}'")
	public Collection<Artista> consultarPorNomeExato(String nome) throws RepositoryException {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Artista> query = criteriaBuilder.createQuery(Artista.class);
		Root<Artista> root = query.from(Artista.class);
		query.where(criteriaBuilder.equal(criteriaBuilder.lower(root.get(Artista_.nome)), nome.toLowerCase()));
		return entityManager.createQuery(query).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@ExceptionWrapper(para = RepositoryException.class, mensagem = "Erro ao consultar artistas")
	public Collection<Artista> consultarPor(Artista exemplo) throws RepositoryException {
		return entityManager.unwrap(Session.class).createCriteria(Artista.class)
				.add(Example.create(exemplo).enableLike(MatchMode.ANYWHERE).excludeZeroes().ignoreCase()).list();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao adicionar artista: {0}")
	public void adicionar(Artista artista) throws ElementoJaExistenteException, RepositoryException {
		logger.debug("Verificando se já existe um artista com o mesmo nome: {}", artista);
		if (!consultarPorNomeExato(artista.getNome()).isEmpty()) {
			throw new ElementoJaExistenteException(artista);
		}

		try {
			logger.debug("Persistindo artista");
			entityManager.persist(artista);
		} catch (EntityExistsException exception) {
			throw new ElementoJaExistenteException(artista);
		}
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao remover artista: {0}")
	public boolean remover(Artista artista) throws RepositoryException {
		logger.debug("Verificando se o artista existe no repositório: {}", artista);
		if (entityManager.contains(artista)) {
			logger.debug("Removendo artista");
			entityManager.remove(artista);
			return true;
		} else {
			logger.debug("O artista já não existia no repositório");
			return false;
		}
	}
}
