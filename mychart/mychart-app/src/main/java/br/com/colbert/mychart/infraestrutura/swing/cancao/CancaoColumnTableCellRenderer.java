package br.com.colbert.mychart.infraestrutura.swing.cancao;

import javax.inject.Inject;
import javax.swing.table.*;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.formatter.CancaoFormatter;

/**
 * Implementação de {@link TableCellRenderer} para uma célula de tabela que represente uma {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
public class CancaoColumnTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -8502095503985878990L;

	@Inject
	private CancaoFormatter cancaoFormatter;

	@Override
	protected void setValue(Object value) {
		super.setValue(cancaoFormatter.format((Cancao) value));
	}
}
