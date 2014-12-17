package br.com.colbert.mychart.dominio.artista.repository;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.criterion.*;

import br.com.colbert.mychart.dominio.artista.Artista;
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
	@SuppressWarnings("unchecked")
	@ExceptionWrapper(para = RepositoryException.class, mensagem = "Erro ao consultar artistas")
	public Collection<Artista> consultarPor(Artista exemplo) throws RepositoryException {
		return entityManager.unwrap(Session.class).createCriteria(Artista.class)
				.add(Example.create(exemplo).enableLike(MatchMode.ANYWHERE).excludeZeroes().ignoreCase()).list();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao adicionar artista: {0}")
	public void adicionar(Artista entidade) throws ElementoJaExistenteException, RepositoryException {
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
