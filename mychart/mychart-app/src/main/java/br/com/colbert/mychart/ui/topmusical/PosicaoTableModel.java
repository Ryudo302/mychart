package br.com.colbert.mychart.ui.topmusical;

import java.util.*;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.topmusical.Posicao;
import br.com.colbert.mychart.infraestrutura.swing.model.*;

/**
 * Modelo de tabela de {@link Posicao}.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
public class PosicaoTableModel extends ObjectTableModel<Posicao> {

	private static final long serialVersionUID = -7220500763253100774L;

	@Override
	protected List<ObjectTableModelColumn<?>> makeColumns() {
		List<ObjectTableModelColumn<?>> colunas = new ArrayList<>(2);

		colunas.add(ObjectTableModelColumn.<Integer> newColumn().withName("#").withTheValueOf("numero").type(Integer.class));
		colunas.add(ObjectTableModelColumn.<Cancao> newColumn().withName("Canção").withTheValueOf("cancao").type(Cancao.class));

		return colunas;
	}
}
