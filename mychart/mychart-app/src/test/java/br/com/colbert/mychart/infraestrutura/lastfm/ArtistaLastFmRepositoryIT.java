package br.com.colbert.mychart.infraestrutura.lastfm;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes da {@link ArtistaLastFmRepository}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaLastFmRepositoryIT extends AbstractTest {

	@Inject
	private ArtistaLastFmRepository repository;

	@Test
	public void testConsultarPor() throws RepositoryException {
		Set<Artista> artistas = repository.consultarPor("rihanna");

		assertThat(artistas, is(notNullValue(Set.class)));
		assertThat(artistas.size(), is(not(equalTo(0))));

		System.out.println(artistas);
	}
}
