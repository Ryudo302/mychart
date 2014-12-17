package br.com.colbert.mychart.dominio.artista.repository;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.criterion.*;

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
	public void adicionar(Artista entidade) throws ElementoJaExistenteException, RepositoryException {
		if (!consultarPorNomeExato(entidade.getNome()).isEmpty()) {
			throw new ElementoJaExistenteException(entidade);
		}

		try {
			entityManager.persist(entidade);
		} catch (EntityExistsException exception) {
			throw new ElementoJaExistenteException(entidade);
		}
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao atualizar artista: {0}")
	public Artista atualizar(Artista entidade) throws ElementoNaoExistenteException, RepositoryException {
		return entityManager.merge(entidade);
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao remover artista: {0}")
	public boolean remover(Artista entidade) throws RepositoryException {
		if (entityManager.contains(entidade)) {
			entityManager.remove(entidade);
			return true;
		} else {
			return false;
		}
	}
}
