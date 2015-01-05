package br.com.colbert.mychart.ui.topmusical;

import java.awt.Container;
import java.awt.event.*;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.ButtonFactory;
import br.com.colbert.mychart.dominio.topmusical.TopMusical;

/**
 * Implementação de {@link TopMusicalView} utilizando Swing.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@ApplicationScoped
public class TopMusicalSwingView implements TopMusicalView, Serializable {

	private static final long serialVersionUID = -6153457716329302059L;

	private TopMusical topAtual;

	private JPanel panel;
	private JTable posicoesTable;

	@Inject
	private CancaoColumnTableCellRenderer cancaoColumnTableCellRenderer;

	private JButton anteriorButton;
	private JButton proximoButton;

	@Inject
	@Any
	private Event<TopMusicalView> ouvintesView;

	public static void main(String[] args) {
		new TopMusicalSwingView().initPanel();
	}

	/**
	 * Inicializa todos os componentes da tela.
	 */
	@PostConstruct
	protected void init() {
		initPanel();
		initComponents();
	}

	private void initPanel() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent event) {
				ouvintesView.fire(TopMusicalSwingView.this);
			}
		});

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel.add(infoPanel);
		infoPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("196px"), ColumnSpec.decode("57px"), },
				new RowSpec[] { RowSpec.decode("21px"), RowSpec.decode("14px"), }));

		JPanel botoesPanel = new JPanel();
		panel.add(botoesPanel);

		anteriorButton = ButtonFactory.createJButton("Anterior", (String) null);
		anteriorButton.setEnabled(false);
		anteriorButton.addActionListener(event -> {
			topAtual.getAnterior().ifPresent(topAnterior -> setTopMusical(topAnterior));
		});
		botoesPanel.add(anteriorButton);

		proximoButton = ButtonFactory.createJButton("Próximo", (String) null);
		proximoButton.setEnabled(false);
		proximoButton.addActionListener(event -> {
			topAtual.getProximo().ifPresent(topAnterior -> setTopMusical(topAnterior));
		});
		botoesPanel.add(proximoButton);

		JButton salvarButton = ButtonFactory.createJButton("Salvar", (String) null);
		botoesPanel.add(salvarButton);

		JPanel posicoesPanel = new JPanel();
		panel.add(posicoesPanel);

		JScrollPane posicoesTableScrollPane = new JScrollPane();
		posicoesTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		posicoesTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		posicoesPanel.add(posicoesTableScrollPane);

		PosicaoTableModel model = new PosicaoTableModel();
		posicoesTable = new JTable();
		posicoesTable.setModel(model);
		posicoesTable.setFillsViewportHeight(true);

		posicoesTableScrollPane.setViewportView(posicoesTable);
	}

	private void initComponents() {
		posicoesTable.getColumnModel().getColumn(1).setCellRenderer(cancaoColumnTableCellRenderer);
	}

	@Override
	public TopMusical getTopMusical() {
		return topAtual;
	}

	@Override
	public void setTopMusical(TopMusical topMusical) {
		topAtual = topMusical;
		anteriorButton.setEnabled(topAtual.getAnterior().isPresent());
		proximoButton.setEnabled(topAtual.getProximo().isPresent());
	}

	/**
	 * Obtém o {@link Container} utilizado para representar a tela.
	 * 
	 * @return a instância do contêiner AWT
	 */
	public Container getContainer() {
		return panel;
	}
}
