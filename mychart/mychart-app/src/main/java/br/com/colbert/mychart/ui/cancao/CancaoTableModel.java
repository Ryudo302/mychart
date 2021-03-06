package br.com.colbert.mychart.ui.cancao;

import java.util.*;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.swing.model.*;

/**
 * Modelo de tabela de canções.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
public class CancaoTableModel extends ObjectTableModel<Cancao> {

	private static final long serialVersionUID = -785332962189334021L;

	@SuppressWarnings("rawtypes")
	@Override
	protected List<ObjectTableModelColumn<?>> makeColumns() {
		return Arrays.asList(ObjectTableModelColumn.<String> newColumn().withName("#").ordinal(), ObjectTableModelColumn
				.<String> newColumn().withName("Título").withTheValueOf("titulo").type(String.class), ObjectTableModelColumn
				.<List> newColumn().withName("Artista(s)").withTheValueOf("artistas").type(List.class));
	}
}
