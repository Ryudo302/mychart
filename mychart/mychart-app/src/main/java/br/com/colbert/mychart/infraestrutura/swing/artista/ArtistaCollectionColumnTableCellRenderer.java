package br.com.colbert.mychart.infraestrutura.swing.artista;

import java.util.*;

import javax.swing.table.*;

import org.jboss.weld.exceptions.IllegalArgumentException;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.formatter.ArtistaStringBuilder;

/**
 * Implementação de {@link TableCellRenderer} para uma célula de tabela que represente uma coleção de {@link Artista}s.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
public class ArtistaCollectionColumnTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -8502095503985878990L;

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object value) {
		if (Objects.isNull(value)) {
			super.setValue(value);
		} else if (value instanceof Collection) {
			super.setValue(new ArtistaStringBuilder().appendAll((Collection<Artista>) value).toString());
		} else {
			throw new IllegalArgumentException("Impossível converter para String: " + value);
		}
	}
}