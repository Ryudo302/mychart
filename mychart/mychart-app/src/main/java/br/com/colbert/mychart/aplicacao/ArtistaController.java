package br.com.colbert.mychart.aplicacao;

import java.util.Collection;

import javax.inject.Inject;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.dominio.artista.repository.ArtistaRepositoryLocal;
import br.com.colbert.mychart.dominio.artista.service.ConsultaArtistaService;
import br.com.colbert.mychart.infraestrutura.exception.ServiceException;
import br.com.colbert.mychart.ui.artista.ArtistaView;

/**
 * Controlador de ações referentes a artistas.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaController extends AbstractEntidadeCrudController<Artista, Integer> {

	@Inject
	private ConsultaArtistaService consultaArtistaService;

	@Inject
	public ArtistaController(ArtistaView view, ArtistaRepositoryLocal repositorio) {
		super(view, repositorio);
	}

	@Override
	protected Collection<Artista> doConsultar(Artista exemplo) throws ServiceException {
		return consultaArtistaService.consultarPor(exemplo);
	}
}
