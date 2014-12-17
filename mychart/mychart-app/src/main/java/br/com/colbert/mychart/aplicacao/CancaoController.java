package br.com.colbert.mychart.aplicacao;

import javax.inject.Inject;

import br.com.colbert.mychart.dominio.cancao.*;
import br.com.colbert.mychart.ui.cancao.CancaoView;

/**
 * Controlador de ações referentes a canções.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 */
public class CancaoController extends AbstractEntidadeCrudController<Cancao, Integer> {

	//@Inject
	public CancaoController(CancaoView view, CancaoRepository repositorio) {
		super(view, repositorio);
	}
}
