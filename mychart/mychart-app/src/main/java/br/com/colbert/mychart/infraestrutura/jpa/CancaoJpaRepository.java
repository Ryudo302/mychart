package br.com.colbert.mychart.infraestrutura.jpa;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.*;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.cancao.*;
import br.com.colbert.mychart.dominio.cancao.repository.CancaoRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapper;
import br.com.colbert.mychart.infraestrutura.jpa.helper.JpaCrudHelper;

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
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao consultar canções")
	public Collection<Cancao> consultarPor(Cancao exemplo) throws RepositoryException {
		return JpaCrudHelper.findByExample(exemplo, Cancao.class, getEntityManager());
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao salvar canção: {0}")
	public Cancao incluirOuAlterar(Cancao cancao) throws RepositoryException {
		logger.debug("Salvando canção");
		return JpaCrudHelper.saveOrUpdate(cancao, getEntityManager());
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao remover canção pelo ID: {0}")
	public boolean remover(String id) throws RepositoryException {
		return JpaCrudHelper.remove(id, Cancao.class, getEntityManager());
	}
}
