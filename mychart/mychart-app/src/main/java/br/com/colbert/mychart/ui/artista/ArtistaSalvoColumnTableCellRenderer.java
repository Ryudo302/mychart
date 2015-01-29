package br.com.colbert.mychart.ui.artista;

import java.awt.Component;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.table.*;

import org.apache.commons.lang3.StringUtils;

import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.providers.ImagesProvider;

/**
 * Implementação de {@link TableCellRenderer} para uma célula de tabela que informa se um {@link Artista} está salvo ou não.
 * 
 * @author Thiago Colbert
 * @since 24/01/2015
 */
@Dependent
public class ArtistaSalvoColumnTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 4294291277049938994L;

	@Inject
	private ImagesProvider images;

	@PostConstruct
	protected void init() {
		setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText(StringUtils.EMPTY);

		if ((Boolean) value) {
			label.setIcon(images.loadImageAsIcon("check.png"));
			label.setToolTipText("Sim");
		} else {
			label.setIcon(images.loadImageAsIcon("uncheck.png"));
			label.setToolTipText("Não");
		}

		return label;
	}
}
