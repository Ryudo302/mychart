package br.com.colbert.mychart.ui.topmusical;

import java.time.LocalDate;
import java.util.List;

import br.com.colbert.base.ui.View;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.ui.comum.CausaSaidaDeView;

/**
 * Tela de configuração do primeiro top musical a ser criado.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
public interface PrimeiroTopMusicalView extends View {

	/**
	 * Torna a janela visivel.
	 * 
	 * @return a causa do fechamento da janela
	 */
	CausaSaidaDeView show();

	/**
	 * Fecha a janela.
	 */
	void close();

	/**
	 * Obtém a data inicial definida para o primeiro top musical.
	 * 
	 * @return a data informada
	 */
	LocalDate getDataInicial();

	/**
	 * Obtém as canções que farão parte do primeiro top musical.
	 * 
	 * @return as canções
	 */
	List<Cancao> getCancoes();
}
