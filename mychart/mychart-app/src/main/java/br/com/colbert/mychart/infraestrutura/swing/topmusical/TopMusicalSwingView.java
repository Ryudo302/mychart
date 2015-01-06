package br.com.colbert.mychart.infraestrutura.swing.topmusical;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.ButtonFactory;
import br.com.colbert.mychart.dominio.IntervaloDeDatas;
import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.infraestrutura.swing.cancao.CancaoColumnTableCellRenderer;
import br.com.colbert.mychart.ui.topmusical.TopMusicalView;

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
	private JButton salvarButton;
	private JLabel dataInicioFimLabel;

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
		infoPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormSpecs.PARAGRAPH_GAP_ROWSPEC, FormSpecs.PREF_ROWSPEC, FormSpecs.PARAGRAPH_GAP_ROWSPEC, }));

		JLabel periodoLabel = new JLabel("Período:");
		infoPanel.add(periodoLabel, "2, 2, fill, top");

		dataInicioFimLabel = new JLabel("-");
		infoPanel.add(dataInicioFimLabel, "4, 2");

		JPanel botoesPanel = new JPanel();
		panel.add(botoesPanel);

		anteriorButton = ButtonFactory.createJButton("Anterior", (String) null);
		anteriorButton.setEnabled(false);
		anteriorButton.addActionListener(event -> setTopMusical(topAtual.getAnterior()));
		botoesPanel.add(anteriorButton);

		proximoButton = ButtonFactory.createJButton("Próximo", (String) null);
		proximoButton.setEnabled(false);
		proximoButton.addActionListener(event -> setTopMusical(topAtual.getProximo()));
		botoesPanel.add(proximoButton);

		salvarButton = ButtonFactory.createJButton("Salvar", (String) null);
		salvarButton.setEnabled(false);
		botoesPanel.add(salvarButton);

		JPanel posicoesPanel = new JPanel();
		panel.add(posicoesPanel);
		posicoesPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane posicoesTableScrollPane = new JScrollPane();
		posicoesTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		posicoesPanel.add(posicoesTableScrollPane);

		PosicaoTableModel model = new PosicaoTableModel();
		posicoesTable = new JTable();
		posicoesTable.setEnabled(false);
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
	public void setTopMusical(Optional<TopMusical> topMusical) {
		topMusical.ifPresent(top -> {
			topAtual = top;
			
			IntervaloDeDatas periodo = top.getPeriodo();
			dataInicioFimLabel.setText(periodo.getDataInicial() + " à " + periodo.getDataFinal());
			
			salvarButton.setEnabled(true);
			anteriorButton.setEnabled(topAtual.getAnterior().isPresent());
			proximoButton.setEnabled(topAtual.getProximo().isPresent());
			posicoesTable.setEnabled(true);
		});
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