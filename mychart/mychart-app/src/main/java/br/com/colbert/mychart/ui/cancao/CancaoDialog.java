package br.com.colbert.mychart.ui.cancao;

import java.awt.*;
import java.awt.event.WindowListener;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.*;
import br.com.colbert.mychart.aplicacao.cancao.CancaoPresenter;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * Janela de canções.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
@Singleton
@MVP(modelClass = Cancao.class, presenterClass = CancaoPresenter.class)
public class CancaoDialog implements FormView<Cancao>, WindowView, Serializable {

	private static final long serialVersionUID = 8909053142047968045L;

	@Action(name = "sair", EventAction = "windowClosing", EventType = WindowListener.class)
	private JDialog dialog;

	private JList<Cancao> cancoesList;

	@Model(property = "id")
	private JTextField idTextField;
	@Model(property = "titulo")
	private JTextField tituloTextField;
	private JTextField artistaPrincipalTextField; // TODO Auto-complete

	@Action(name = "consultarCancoes")
	private JButton consultarButton;
	@Action(name = "selecionarCancoes")
	private JButton selecionarButton;

	@Inject
	private MainWindow mainWindow;
	@Inject
	private CancaoListCellRenderer cancaoListCellRenderer;

	public static void main(String[] args) {
		new CancaoDialog().initPanel();
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
		dialog = new JDialog(mainWindow != null ? mainWindow.getFrame() : null, "Consulta de Canções", true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setPreferredSize(new Dimension(400, 270));

		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel informacoesPanel = new JPanel();
		informacoesPanel.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es", TitledBorder.LEFT, TitledBorder.TOP, null,
				null));
		informacoesPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("3dlu:grow"), FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormSpecs.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.PARAGRAPH_GAP_ROWSPEC, }));

		JLabel tituloLabel = new JLabel("Título:");
		informacoesPanel.add(tituloLabel, "2, 2, left, center");

		contentPane.add(informacoesPanel);

		idTextField = new JTextField();
		idTextField.setVisible(false);

		tituloTextField = new JTextField();
		tituloTextField.setToolTipText("O título da canção");
		tituloTextField.addActionListener(event -> consultarButton.doClick());
		informacoesPanel.add(tituloTextField, "4, 2, fill, default");

		JLabel artistaPrincipalLabel = new JLabel("Artista Principal:");
		informacoesPanel.add(artistaPrincipalLabel, "2, 4, right, center");

		artistaPrincipalTextField = new JTextField();
		artistaPrincipalTextField.setToolTipText("O artista principal da canção.");
		informacoesPanel.add(artistaPrincipalTextField, "4, 4, fill, default");
		artistaPrincipalTextField.setColumns(10);

		JPanel botoesPanel = new JPanel();

		consultarButton = ButtonFactory.createJButton("Consultar", "Procura por canções com o título acima");
		botoesPanel.add(consultarButton);

		contentPane.add(botoesPanel);

		selecionarButton = ButtonFactory.createJButton("Selecionar", "Seleciona a canção");
		botoesPanel.add(selecionarButton);

		CancoesListModel cancoesListModel = new CancoesListModel();
		cancoesList = new JList<>(cancoesListModel);

		JScrollPane cancoesListScrollPane = new JScrollPane(cancoesList);
		cancoesListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(cancoesListScrollPane);

		tituloTextField.requestFocusInWindow();

		dialog.pack();
	}

	private void initComponents() {
		cancoesList.setCellRenderer(cancaoListCellRenderer);
	}

	public List<Cancao> getCancoesSelecionadas() {
		return cancoesList.getSelectedValuesList();
	}

	public String getIdCancao() {
		return idTextField.getText();
	}

	public String getTituloCancao() {
		return tituloTextField.getText();
	}

	public String getArtistaPrincipal() {
		return artistaPrincipalTextField.getText();
	}

	@Override
	public void setConteudoTabela(Collection<Cancao> cancoes) {
		SwingUtils.invokeLater(() -> ((CancoesListModel) cancoesList.getModel()).setElements(cancoes));
	}

	@Override
	public void limparTela() {
		SwingUtils.clearAllData(dialog.getContentPane());
		tituloTextField.requestFocusInWindow();
	}

	@Override
	public void show() {
		// não usa super.show() para permitir que a thread fique interrompida enquanto a janela estiver aberta
		dialog.setVisible(true);
	}

	@Override
	public Container getContainer() {
		return dialog;
	}
}
