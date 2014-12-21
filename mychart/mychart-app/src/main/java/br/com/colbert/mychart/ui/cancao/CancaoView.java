package br.com.colbert.mychart.ui.cancao;

import java.util.Collection;

import br.com.colbert.base.ui.View;
import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Uma view de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public interface CancaoView extends View {

	/**
	 * Define a canção atualmente sendo exibida em detalhes na visão.
	 * 
	 * @param cancao
	 */
	void setCancaoAtual(Cancao cancao);

	/**
	 * Define as canções a serem listadas na visão.
	 * 
	 * @param cancoes
	 */
	void setCancoes(Collection<Cancao> cancoes);
}
