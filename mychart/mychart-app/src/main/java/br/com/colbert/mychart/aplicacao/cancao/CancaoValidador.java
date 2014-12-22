package br.com.colbert.mychart.aplicacao.cancao;

import javax.inject.Inject;
import javax.validation.Validator;

import br.com.colbert.base.aplicacao.validacao.EntidadeValidador;
import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Validador de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 20/12/2014
 */
public class CancaoValidador extends EntidadeValidador<Cancao> {

	private static final long serialVersionUID = 3250614642014513069L;

	@Inject
	public CancaoValidador(Validator validator) {
		super(validator);
	}
}
