package br.com.colbert.mychart.infraestrutura.jpa;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.*;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepository;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.mychart.infraestrutura.interceptors.ExceptionWrapper;
import br.com.colbert.mychart.infraestrutura.jpa.helper.*;

/**
 * Uma implementação de {@link ArtistaRepository} que utiliza o JPA.
 *
 * @author Thiago Colbert
 * @since 15/12/2014
 */
public class ArtistaJpaRepository implements ArtistaRepository {

	private final class RegraExclusaoArtista implements RegraValidacao<Artista>, Serializable {

		private static final long serialVersionUID = -2241245076310066965L;

		private static final String MENSAGEM = "O artista não pode ser removido pois existem canções que o referenciam";

		@Override
		public String getMensagemErro() {
			return MENSAGEM;
		}

		@Override
		public boolean isValido(Artista objeto) {
			return !objeto.getPossuiCancoes();
		}
	}

	@Inject
	private Logger logger;

	@Inject
	private Instance<EntityManager> entityManager;

	private EntityManager getEntityManager() {
		return entityManager.get();
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao consultar artistas pelo nome: '{0}'")
	public Collection<Artista> consultarPorNomeExato(String nome) throws RepositoryException {
		return JpaCrudHelper.findByProperty(Artista_.nome, nome, Artista.class, getEntityManager());
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao consultar artistas")
	public Collection<Artista> consultarPor(Artista exemplo) throws RepositoryException {
		return JpaCrudHelper.findByExample(exemplo, getEntityManager());
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao salvar artista: {0}")
	public Artista incluirOuAlterar(Artista artista) throws RepositoryException {
		logger.debug("Persistindo artista");
		return JpaCrudHelper.saveOrUpdate(artista, getEntityManager());
	}

	@Override
	@ExceptionWrapper(de = PersistenceException.class, para = RepositoryException.class, mensagem = "Erro ao remover artista pelo ID: {0}")
	public boolean remover(String id) throws RepositoryException {
		return JpaCrudHelper.remove(id, Artista.class, getEntityManager(), new RegraExclusaoArtista());
	}
}
