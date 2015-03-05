package br.com.colbert.mychart.infraestrutura.jpa.helper;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.*;
import org.hibernate.criterion.*;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Classe auxiliar para operações de CRUD sobre entidades JPA.
 *
 * @author Thiago Colbert
 * @since 04/03/2015
 */
public class JpaCrudHelper implements Serializable {

	private static final long serialVersionUID = 7158114710529542039L;

	/**
	 * Salva ou atualiza uma entidade, dependendo do seu estado atual.
	 *
	 * @param entidade
	 *            a ser salva ou atualizada
	 * @param entityManager
	 * @return a entidade após a execução do método
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 * @throws PersistenceException
	 *             caso ocorra algum erro ao realizar a consulta
	 */
	public static <T extends Entidade<?>> T saveOrUpdate(T entidade, EntityManager entityManager) {
		return notNull(entityManager).merge(notNull(entidade));
	}

	/**
	 * Remove uma entidade a partir de seu ID.
	 *
	 * @param id
	 *            ID da entidade a ser removida
	 * @param classe
	 *            classe da entidade a ser removida
	 * @param entityManager
	 * @param regraExclusao
	 *            regra a ser aplicada para validar a exclusão da entidade
	 * @return <code>true</code> caso alguma entidade tenha sido removida, <code>false</code> caso contrário
	 * @throws NullPointerException
	 *             caso o ID, a classe ou o <em>EntityManager</em> sejam <code>null</code>
	 * @throws PersistenceException
	 *             caso ocorra algum erro ao remover a entidade
	 * @see #remove(Serializable, Class, EntityManager)
	 */
	public static <T extends Entidade<ID>, ID extends Serializable> boolean remove(ID id, Class<T> classe, EntityManager entityManager,
			RegraValidacao<T> regraExclusao) {
		boolean removido = false;
		T entidade = notNull(entityManager).find(notNull(classe), notNull(id));
		if (entidade != null) {
			if (podeExcluir(regraExclusao, entidade)) {
				entityManager.remove(entidade);
				removido = true;
			} else {
				throw new PersistenceException(regraExclusao.getMensagemErro());
			}
		}
		return removido;
	}

	/**
	 * Remove uma entidade a partir de seu ID.
	 *
	 * @param id
	 *            ID da entidade a ser removida
	 * @param classe
	 *            classe da entidade a ser removida
	 * @param entityManager
	 * @return <code>true</code> caso alguma entidade tenha sido removida, <code>false</code> caso contrário
	 * @throws NullPointerException
	 *             caso o ID, a classe ou o <em>EntityManager</em> sejam <code>null</code>
	 * @throws PersistenceException
	 *             caso ocorra algum erro ao remover a entidade
	 */
	public static <T extends Entidade<ID>, ID extends Serializable> boolean remove(ID id, Class<T> classe, EntityManager entityManager) {
		return remove(id, classe, entityManager, null);
	}

	private static <T extends Entidade<?>> boolean podeExcluir(RegraValidacao<T> regraExclusao, T entidade) {
		return regraExclusao != null ? regraExclusao.isValido(entidade) : true;
	}

	/**
	 * Consulta elementos a partir de um exemplo.
	 *
	 * @param exemplo
	 *            a ser utilizado na consulta
	 * @param entityManager
	 * @return os elementos encontrados (pode estar vazia, mas nunca será <code>null</code>)
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 * @throws PersistenceException
	 *             caso ocorra algum erro ao realizar a consulta
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entidade<?>> Collection<T> findByExample(T exemplo, EntityManager entityManager) {
		try {
			return notNull(entityManager).unwrap(Session.class).createCriteria(Cancao.class)
					.add(Example.create(Objects.requireNonNull(exemplo, "Exemplo")).enableLike(MatchMode.ANYWHERE).excludeZeroes().ignoreCase())
					.list();
		} catch (HibernateException exception) {
			throw new PersistenceException(exception);
		}
	}

	/**
	 * Consulta elementos a partir do valor de uma propriedade.
	 *
	 * @param propriedade
	 * @param valor
	 * @param classe
	 *            classe da entidade
	 * @param entityManager
	 * @return os elementos encontrados (pode estar vazia, mas nunca será <code>null</code>)
	 * @throws NullPointerException
	 *             caso a propriedade, a classe ou o <em>EntityManager</em> sejam <code>null</code>
	 * @throws PersistenceException
	 *             caso ocorra algum erro ao realizar a consulta
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entidade<?>, V> Collection<T> findByProperty(SingularAttribute<T, V> propriedade, V valor, Class<T> classe,
			EntityManager entityManager) {
		Objects.requireNonNull(valor, "Valor da propriedade");
		CriteriaBuilder criteriaBuilder = notNull(entityManager).getCriteriaBuilder();
		CriteriaQuery<T> query = criteriaBuilder.createQuery(notNull(classe));
		Root<T> root = query.from(classe);

		if (valor instanceof String) {
			query.where(criteriaBuilder.equal(criteriaBuilder.lower((Expression<String>) root.get(propriedade)), ((String) valor).toLowerCase()));
		} else {
			query.where(criteriaBuilder.equal(root.get(propriedade), valor));
		}

		return entityManager.createQuery(query).getResultList();
	}

	private static <T extends Entidade<?>> T notNull(T entidade) {
		return Objects.requireNonNull(entidade, "Entidade");
	}

	private static <ID extends Serializable> ID notNull(ID id) {
		return Objects.requireNonNull(id, "ID");
	}

	private static <T extends Entidade<?>> Class<T> notNull(Class<T> classe) {
		return Objects.requireNonNull(classe, "Classe");
	}

	private static EntityManager notNull(EntityManager entityManager) {
		return Objects.requireNonNull(entityManager, "EntityManager");
	}
}
