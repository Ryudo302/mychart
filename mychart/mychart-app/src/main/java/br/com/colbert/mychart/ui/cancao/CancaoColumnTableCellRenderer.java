package br.com.colbert.mychart.ui.cancao;

import javax.inject.Inject;
import javax.swing.table.*;

import org.slf4j.Logger;

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
	@Inject
	private Logger logger;

	@Override
	protected void setValue(Object value) {
		logger.trace("Formatando para exibição: {}", value);
		super.setValue(value != null ? cancaoFormatter.format((Cancao) value) : value);
	}
}
