package br.com.colbert.mychart.dominio.artista.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.junit.Test;
import org.mockito.Mock;

import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.dominio.artista.repository.*;
import br.com.colbert.mychart.infraestrutura.eventos.artista.ModoConsulta;
import br.com.colbert.mychart.infraestrutura.exception.*;
import br.com.colbert.tests.support.AbstractTest;

/**
 * Testes unitários da classe {@link ConsultaArtistaService}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaServiceTest extends AbstractTest {

	@Inject
	private ConsultaArtistaService artistaService;

	@Produces
	@Mock
	private ArtistaRepositoryLocal artistaRepositoryLocal;
	@Mock
	@Produces
	private ArtistaRepositoryRemoto artistaRepositoryRemoto;

	@Test
	public void testConsultarPorNome() throws ServiceException, RepositoryException {
		Artista artista = new Artista("Teste", TipoArtista.MASCULINO_SOLO);

		artistaService.consultarPor(artista, ModoConsulta.TODOS);

		verify(artistaRepositoryLocal, times(1)).consultarPor(artista);
		verify(artistaRepositoryRemoto, times(1)).consultarPor(artista.getNome());
	}
}
