package br.com.colbert.mychart.ui.topmusical;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.*;
import br.com.colbert.mychart.aplicacao.topmusical.PrimeiroTopMusicalPresenter;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.dominio.topmusical.TopMusicalConfiguration;
import br.com.colbert.mychart.infraestrutura.swing.formatter.LocalDateFormatter;
import br.com.colbert.mychart.ui.artista.ArtistaCollectionColumnTableCellRenderer;
import br.com.colbert.mychart.ui.cancao.CancaoTableModel;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * Tela de configuração do primeiro top musical a ser criado.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = PrimeiroTopMusicalPresenter.class)
public class PrimeiroTopMusicalDialog implements WindowView, Serializable {

	private static final long serialVersionUID = -619331878800317148L;

	private JDialog dialog;

	@Inject
	private MainWindow mainWindow;

	private JPanel infoPanel;
	private JPanel botoesPanel;

	private JFormattedTextField dataInicialFormattedTextField;
	private JLabel observacaoLabel;
	private JLabel cancoesLabel;
	private JLabel previewLabel;

	@Action(name = "adicionarCancao")
	private JButton adicionarCancaoButton;
	@Action(name = "removerCancao")
	private JButton removerCancaoButton;
	@Action(name = "moveCancaoParaCima")
	private JButton upButton;
	@Action(name = "moverCancaoParaBaixo")
	private JButton downButton;

	private JTable cancoesTable;

	@Action(name = "salvar")
	private JButton salvarButton;
	@Action(name = "cancelar")
	private JButton cancelarButton;

	@Inject
	private TopMusicalConfiguration configuration;
	@Inject
	private LocalDateFormatter localDateFormatter;
	@Inject
	private ArtistaCollectionColumnTableCellRenderer artistaColumnTableCellRenderer;

	public static void main(String[] args) {
		new PrimeiroTopMusicalDialog().initDialog();
	}

	/**
	 * Inicializa todos os componentes visuais da tela.
	 */
	@PostConstruct
	protected void init() {
		initDialog();
		initComponents();
	}

	private void initDialog() {
		dialog = new JDialog(mainWindow != null ? mainWindow.getFrame() : null, "Primeiro Top Musical", true);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

		Container contentPane = dialog.getContentPane();

		infoPanel = new JPanel();
		infoPanel
				.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, ColumnSpec.decode("3dlu:grow"),
						FormSpecs.DEFAULT_COLSPEC, }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("11px"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("21px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("14px"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("125px"), }));

		JLabel dataInicialLabel = new JLabel("Data:");
		infoPanel.add(dataInicialLabel, "2, 2, left, center");

		dataInicialFormattedTextField = new JFormattedTextField();
		dataInicialFormattedTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				if (!Character.isDigit(event.getKeyChar())) {
					event.consume();
				}
			}
		});
		dataInicialFormattedTextField.setToolTipText("A data inicial do top musical");
		dataInicialFormattedTextField.setFocusLostBehavior(JFormattedTextField.COMMIT);
		infoPanel.add(dataInicialFormattedTextField, "4, 2, fill, top");

		String observacao = "Obs.: seu top está configurado para ser {0}. Para alterar isso, acesse as configurações de top musical.";
		observacaoLabel = new JLabel(observacao);
		observacaoLabel.setToolTipText(observacao);
		observacaoLabel.setFont(new Font("Tahoma", Font.PLAIN, 9));
		infoPanel.add(observacaoLabel, "2, 4, 10, 1, fill, top");

		cancoesLabel = new JLabel("Canções:");
		infoPanel.add(cancoesLabel, "2, 6, left, center");

		adicionarCancaoButton = new JButton("Adicionar");
		adicionarCancaoButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		adicionarCancaoButton.setToolTipText("Adiciona uma canção ao top");
		infoPanel.add(adicionarCancaoButton, "4, 6, left, top");

		removerCancaoButton = new JButton("Remover");
		removerCancaoButton.setEnabled(false);
		removerCancaoButton.setToolTipText("Remove a canção selecionada");
		removerCancaoButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		infoPanel.add(removerCancaoButton, "6, 6, left, top");

		upButton = new JButton("Up");
		upButton.setEnabled(false);
		upButton.setToolTipText("Move a canção selecionada uma posição acima");
		upButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		infoPanel.add(upButton, "8, 6");

		downButton = new JButton("Down");
		downButton.setEnabled(false);
		downButton.setToolTipText("Move a canção selecionada uma posição abaixo");
		downButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		infoPanel.add(downButton, "10, 6");

		previewLabel = new JLabel("Preview:");
		infoPanel.add(previewLabel, "2, 8, left, top");

		CancaoTableModel model = new CancaoTableModel();
		model.addTableModelListener(event -> adicionarCancaoButton.setEnabled(model.getRowCount() < configuration
				.getQuantidadePosicoes()));

		cancoesTable = new JTable();
		cancoesTable.setToolTipText("Preview de como ficará o seu primeiro top musical");
		cancoesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cancoesTable.setEnabled(false);
		cancoesTable.setModel(model);
		cancoesTable.setFillsViewportHeight(true);

		cancoesTable.getSelectionModel().addListSelectionListener(event -> {
			if (!event.getValueIsAdjusting()) {
				boolean algumaLinhaSelecionada = cancoesTable.getSelectedRowCount() > 0;
				removerCancaoButton.setEnabled(algumaLinhaSelecionada);
				upButton.setEnabled(algumaLinhaSelecionada);
				downButton.setEnabled(algumaLinhaSelecionada);
			}
		});

		JScrollPane cancoesTableScrollPane = new JScrollPane();
		cancoesTableScrollPane.setViewportView(cancoesTable);
		infoPanel.add(cancoesTableScrollPane, "4, 10, 8, 1, fill, fill");

		botoesPanel = new JPanel();

		salvarButton = ButtonFactory.createJButton("Salvar", "Salva o top musical");
		botoesPanel.add(salvarButton);

		cancelarButton = ButtonFactory.createJButton("Cancelar", "Cancelar criação do top musical");
		botoesPanel.add(cancelarButton);

		contentPane.add(infoPanel);
		contentPane.add(botoesPanel);

		dataInicialFormattedTextField.requestFocusInWindow();

		dialog.pack();
	}

	private void initComponents() {
		dialog.setLocationRelativeTo(mainWindow.getFrame());

		dataInicialFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(localDateFormatter));
		dataInicialFormattedTextField.setValue(LocalDate.now());

		observacaoLabel.setText(MessageFormat.format(observacaoLabel.getText(), configuration.getFrequencia().getDescricao()
				.toLowerCase()));
		observacaoLabel.setToolTipText(observacaoLabel.getText());

		cancoesTable.getColumnModel().getColumn(2).setCellRenderer(artistaColumnTableCellRenderer);
	}

	/**
	 * Obtém a data inicial definida para o primeiro top musical.
	 * 
	 * @return a data informada
	 */
	public LocalDate getDataInicial() {
		return (LocalDate) dataInicialFormattedTextField.getValue();
	}

	public List<Cancao> getCancoes() {
		return ((CancaoTableModel) cancoesTable.getModel()).getElements();
	}

	@Override
	public Container getContainer() {
		return dialog;
	}
}
