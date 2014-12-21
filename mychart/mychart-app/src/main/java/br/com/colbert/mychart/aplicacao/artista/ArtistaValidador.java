package br.com.colbert.mychart.aplicacao.artista;

import javax.inject.Inject;
import javax.validation.Validator;

import br.com.colbert.base.aplicacao.validacao.EntidadeValidador;
import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Validador de {@link Artista}s.
 * 
 * @author Thiago Colbert
 * @since 20/12/2014
 */
public class ArtistaValidador extends EntidadeValidador<Artista> {

	private static final long serialVersionUID = 3250614642014513069L;

	@Inject
	public ArtistaValidador(Validator validator) {
		super(validator);
	}
}
