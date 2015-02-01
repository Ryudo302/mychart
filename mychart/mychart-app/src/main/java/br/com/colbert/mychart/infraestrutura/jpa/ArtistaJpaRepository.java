package br.com.colbert.mychart.infraestrutura.jpa;

import java.util.*;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang3.Validate;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapper;

/**
 * Uma implementação de {@link ArtistaRepository} que utiliza o JPA.
 *
 * @author Thiago Colbert
 * @since 15/12/2014
 */
public class ArtistaJpaRepository implements ArtistaRepository {

	@Inject
	private Logger logger;

	@Inject
	private Instance<EntityManager> entityManager;

	private EntityManager getEntityManager() {
		return entityManager.get();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class,
			para = RepositoryException.class,
			mensagem = "Erro ao consultar artistas pelo nome: '{0}'")
	public Collection<Artista> consultarPorNomeExato(String nome) throws RepositoryException {
		Validate.notBlank(nome, "O nome é obrigatório");
		EntityManager entityManager = getEntityManager();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Artista> query = criteriaBuilder.createQuery(Artista.class);
		Root<Artista> root = query.from(Artista.class);
		query.where(criteriaBuilder.equal(criteriaBuilder.lower(root.get(Artista_.nome)), nome.toLowerCase()));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao consultar artistas")
	public Collection<Artista> consultarPor(Artista exemplo) throws RepositoryException {
		return getEntityManager()
				.unwrap(Session.class)
				.createCriteria(Artista.class)
				.add(Example.create(Objects.requireNonNull(exemplo, "O exemplo é obrigatório")).enableLike(MatchMode.ANYWHERE)
						.excludeZeroes().ignoreCase().excludeProperty("tipo")).list();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao salvar artista: {0}")
	public Artista incluirOuAlterar(Artista artista) throws RepositoryException {
		Objects.requireNonNull(artista, "O artista a ser adicionado é obrigatório");

		logger.debug("Persistindo artista");
		return getEntityManager().merge(artista);
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class,
			para = RepositoryException.class,
			mensagem = "Erro ao remover artista pelo ID: {0}")
	public boolean remover(String id) throws RepositoryException {
		Validate.notEmpty(id, "O ID do artista a ser removido é obrigatório");

		boolean removido = false;
		EntityManager entityManager = getEntityManager();

		logger.debug("Verificando se existe um artista no repositório com o ID: {}", id);
		Artista artista = entityManager.find(Artista.class, id);

		if (artista != null) {
			logger.debug("Verificando se o artista possui canções salvas");
			if (artista.getPossuiCancoes()) {
				throw new RepositoryException("O artista não pode ser removido pois existem canções que o referenciam");
			} else {
				logger.debug("Removendo artista");
				entityManager.remove(artista);
				removido = true;
			}
		}

		logger.debug("Algum artista foi removido? {}", removido);
		return removido;
	}
}
