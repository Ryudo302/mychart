package br.com.colbert.mychart.ui.cancao;

import java.util.*;
import java.util.concurrent.FutureTask;

import br.com.colbert.base.ui.View;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.ui.comum.CausaSaidaDeView;

/**
 * Uma {@link View} de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
public interface CancaoView extends View {

	/**
	 * Torna a janela visivel.
	 * 
	 * @return a causa do fechamento da janela
	 */
	FutureTask<CausaSaidaDeView> show();

	/**
	 * Fecha a janela.
	 */
	void close();

	/**
	 * Obtém a canção cujas informações estão atualmente sendo exibidas na tela.
	 * 
	 * @return a canção
	 */
	Optional<Cancao> getCancaoAtual();

	/**
	 * Obtém as canções que estão selecionadas na tela.
	 * 
	 * @return as canções selecionadas (pode estar vazia)
	 */
	List<Cancao> getCancoesSelecionadas();

	/**
	 * Define as canções disponíveis na tela para seleção.
	 * 
	 * @param cancoes
	 *            as canções
	 */
	void setCancoesDisponiveis(Collection<Cancao> cancoes);
}
