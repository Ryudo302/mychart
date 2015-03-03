package br.com.colbert.mychart.ui.topmusical;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.*;
import br.com.colbert.mychart.aplicacao.topmusical.TopMusicalPresenter;
import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;
import br.com.colbert.mychart.ui.cancao.CancaoColumnTableCellRenderer;
import br.com.colbert.mychart.ui.principal.PainelTelaPrincipal;

/**
 * Painel de Top Musical.
 * 
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@Singleton
@MVP(modelClass = TopMusical.class, presenterClass = TopMusicalPresenter.class)
@PainelTelaPrincipal
public class TopMusicalPanel implements FormView<Posicao>, Serializable {

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
	public void limparTela() {
		SwingUtils.clearAllData(panel);
	}

	@Override
	public void setConteudoTabela(Collection<Posicao> elementos) {
		// TODO Auto-generated method stub
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
	@Override
	public Container getContainer() {
		return panel;
	}
}
