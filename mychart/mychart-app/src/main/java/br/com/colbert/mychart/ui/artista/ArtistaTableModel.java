package br.com.colbert.mychart.ui.artista;

import java.util.*;

import br.com.colbert.base.ui.model.*;
import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Modelo de tabela de {@link Artista}.
 * 
 * @author Thiago Colbert
 */
public class ArtistaTableModel extends ObjectTableModel<Artista> {

	private static final long serialVersionUID = -6848915681331763093L;

	@Override
	protected List<ObjectTableModelColumn<?>> makeColumns() {
		List<ObjectTableModelColumn<?>> colunas = new ArrayList<>(2);

		colunas.add(ObjectTableModelColumn.<String> newColumn().withName("Nome").withTheValueOf("nome").type(String.class));
		colunas.add(ObjectTableModelColumn.<String> newColumn().withName("Tipo").withTheValueOf("tipo").type(String.class));

		return colunas;
	}
}
