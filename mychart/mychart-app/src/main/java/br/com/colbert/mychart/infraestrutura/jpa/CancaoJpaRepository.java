package br.com.colbert.mychart.infraestrutura.jpa;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.*;

import org.apache.commons.lang3.Validate;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.cancao.*;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
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
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao salvar canção: {0}")
	public Cancao incluirOuAlterar(Cancao cancao) throws RepositoryException {
		Objects.requireNonNull(cancao, "A canção a ser adicionada é obrigatória");

		logger.debug("Salvando canção");
		return getEntityManager().merge(cancao);
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao remover canção pelo ID: {0}")
	public boolean remover(String id) throws RepositoryException {
		Validate.notBlank(id, "O ID da canção a ser removida é obrigatório");

		boolean removido = false;
		EntityManager entityManager = getEntityManager();

		logger.debug("Verificando se existe uma canção no repositório com o ID: {}", id);
		Cancao cancao = entityManager.find(Cancao.class, id);
		if (cancao != null) {
			logger.debug("Removendo canção");
			entityManager.remove(cancao);
			removido = true;
		}

		return removido;
	}
}
