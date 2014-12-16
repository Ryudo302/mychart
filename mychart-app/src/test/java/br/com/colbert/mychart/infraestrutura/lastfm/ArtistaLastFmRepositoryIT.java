package br.com.colbert.mychart.infraestrutura.lastfm;

import java.util.Set;

import javax.inject.Inject;

import org.junit.*;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.exception.RepositoryException;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes da {@link ArtistaLastFmRepository}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@Ignore
public class ArtistaLastFmRepositoryIT extends AbstractTest {

	@Inject
	private ArtistaLastFmRepository repository;

	@Test
	public void testConsultarPor() throws RepositoryException {
		Set<Artista> artistas = repository.consultarPor("rihanna");
		System.out.println(artistas);
	}
}
