package br.com.colbert.mychart.infraestrutura.jpa;

import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;

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
	@ExceptionWrapper(para = RepositoryException.class, mensagem = "Erro ao consultar canções pelo título: '{0}'")
	public Collection<Cancao> consultarPorTituloExato(String nome) throws RepositoryException {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Cancao> query = criteriaBuilder.createQuery(Cancao.class);
		Root<Cancao> root = query.from(Cancao.class);
		query.where(criteriaBuilder.equal(criteriaBuilder.lower(root.get(Cancao_.titulo)), nome.toLowerCase()));
		return getEntityManager().createQuery(query).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@ExceptionWrapper(para = RepositoryException.class, mensagem = "Erro ao consultar canções")
	public Collection<Cancao> consultarPor(Cancao exemplo) throws RepositoryException {
		return getEntityManager().unwrap(Session.class).createCriteria(Cancao.class)
				.add(Example.create(exemplo).enableLike(MatchMode.ANYWHERE).excludeZeroes().ignoreCase()).list();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao adicionar canção: {0}")
	public void adicionar(Cancao cancao) throws ElementoJaExistenteException, RepositoryException {
		logger.debug("Verificando se já existe um cancao com o mesmo nome: {}", cancao);
		if (!consultarPorTituloExato(cancao.getTitulo()).isEmpty()) {
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
		logger.debug("Verificando se a canção existe no repositório: {}", cancao);
		if (cancao.getId() != null) {
			logger.debug("Removendo cancao");
			EntityManager entityManager = getEntityManager();
			Cancao cancaoRemover = entityManager.getReference(Cancao.class, cancao.getId());
			entityManager.remove(cancaoRemover);
			return true;
		} else {
			logger.debug("O cancao já não existia no repositório");
			return false;
		}
	}
}