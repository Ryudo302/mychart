package br.com.colbert.mychart.infraestrutura.swing.cancao;

import java.awt.Component;

import javax.inject.Inject;
import javax.swing.*;

import org.slf4j.Logger;

import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.formatter.CancaoFormatter;

/**
 * Implementação de {@link ListCellRenderer} para uma lista de {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 06/01/2015
 */
public class CancaoListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -4069732668937979936L;

	@Inject
	private CancaoFormatter cancaoFormatter;
	@Inject
	private Logger logger;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		logger.trace("Formatando para exibição: {}", value);
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		label.setText(value != null ? cancaoFormatter.format((Cancao) value) : ((JLabel) super.getListCellRendererComponent(list,
				value, index, isSelected, cellHasFocus)).getText());
		return label;
	}
}
