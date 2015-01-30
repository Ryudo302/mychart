package br.com.colbert.mychart.ui.topmusical;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.Serializable;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.mvp4j.annotation.Action;
import org.mvp4j.annotation.MVP;
import org.mvp4j.annotation.Model;

import br.com.colbert.base.ui.ButtonFactory;
import br.com.colbert.mychart.aplicacao.topmusical.TopMusicalPresenter;
import br.com.colbert.mychart.dominio.topmusical.Posicao;
import br.com.colbert.mychart.dominio.topmusical.TopMusical;
import br.com.colbert.mychart.ui.cancao.CancaoColumnTableCellRenderer;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Painel de Top Musical.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@Singleton
@MVP(modelClass = TopMusical.class, presenterClass = TopMusicalPresenter.class)
public class TopMusicalPanel implements Serializable {

	private static final long serialVersionUID = -6153457716329302059L;

	private JPanel panel;

	private JTable posicoesTable;
	private PosicaoTableModel posicoesTableModel;

	@Inject
	private CancaoColumnTableCellRenderer cancaoColumnTableCellRenderer;

	@Action(name = "topAnterior")
	private JButton anteriorButton;
	@Action(name = "proximoTop")
	private JButton proximoButton;
	@Action(name = "salvar")
	private JButton salvarButton;

	@Inject
	@Any
	private Event<TopMusicalPanel> ouvintesView;

	@Model(property = "periodo.dataInicial")
	private JLabel dataInicioLabel;
	@Model(property = "periodo.dataFinal")
	private JLabel dataFimLabel;

	public static void main(String[] args) {
		new TopMusicalPanel().initPanel();
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

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel.add(infoPanel);
		infoPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormSpecs.PARAGRAPH_GAP_ROWSPEC, FormSpecs.PREF_ROWSPEC, FormSpecs.PARAGRAPH_GAP_ROWSPEC, }));

		JLabel periodoLabel = new JLabel("Período:");
		infoPanel.add(periodoLabel, "2, 2, fill, top");

		dataInicioLabel = new JLabel("-");
		JLabel aLabel = new JLabel(" à ");
		dataFimLabel = new JLabel("-");

		infoPanel.add(dataInicioLabel, "4, 2");
		infoPanel.add(aLabel, "6, 2");
		infoPanel.add(dataFimLabel, "8, 2");

		JPanel botoesPanel = new JPanel();
		panel.add(botoesPanel);

		anteriorButton = ButtonFactory.createJButton("Anterior", (String) null);
		anteriorButton.setEnabled(false);
		botoesPanel.add(anteriorButton);

		proximoButton = ButtonFactory.createJButton("Próximo", (String) null);
		proximoButton.setEnabled(false);
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

	/**
	 * Obtém a posição selecionada na tabela.
	 * 
	 * @return a posição
	 */
	public Optional<Posicao> getPosicaoSelecionada() {
		int selectedRow = posicoesTable.getSelectedRow();
		if (selectedRow >= 0) {
			int modelIndex = posicoesTable.convertRowIndexToModel(selectedRow);
			return modelIndex != -1 ? Optional.of(Posicao.copia(posicoesTableModel.getElement(modelIndex))) : Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	public JButton getAnteriorButton() {
		return anteriorButton;
	}

	public JButton getProximoButton() {
		return proximoButton;
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
