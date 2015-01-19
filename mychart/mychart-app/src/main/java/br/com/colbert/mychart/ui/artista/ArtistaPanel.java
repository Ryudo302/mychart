package br.com.colbert.mychart.ui.artista;

import java.awt.Container;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.*;
import br.com.colbert.mychart.aplicacao.artista.ArtistaPresenter;
import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;

/**
 * Painel de artistas.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@Singleton
@MVP(modelClass = Artista.class, presenterClass = ArtistaPresenter.class)
public class ArtistaPanel implements Serializable {

	private static final long serialVersionUID = -7371434021781119641L;

	private JPanel panel;

	@Model(property = "id")
	private JTextField idTextField;

	@Model(property = "nome")
	@Action(name = "consultarArtistas", EventType = ActionListener.class, EventAction = "actionPerformed")
	private JTextField nomeTextField;

	@Model(property = "tipo", initProperty = "tiposArtistas")
	private JComboBox<TipoArtista> tipoComboBox;

	@Actions({ @Action(name = "teclaPressionada", EventType = KeyListener.class, EventAction = "keyPressed"),
			@Action(name = "artistaSelecionado", EventType = ListSelectionListener.class, EventAction = "valueChanged") })
	private JTable artistasTable;

	private ArtistaTableModel artistasTableModel;

	@Action(name = "consultarArtistas")
	private JButton consultarButton;

	@Action(name = "salvarArtista")
	private JButton salvarButton;

	@Action(name = "removerArtista")
	private JButton removerButton;

	public static void main(String[] args) {
		new ArtistaPanel().initPanel();
	}

	@PostConstruct
	protected void init() {
		initPanel();
		initComponents();
	}

	private void initPanel() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel informacoesPanel = new JPanel();
		informacoesPanel.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es", TitledBorder.LEFT, TitledBorder.TOP, null,
				null));
		panel.add(informacoesPanel);
		informacoesPanel.setLayout(new FormLayout(new ColumnSpec[] { com.jgoodies.forms.layout.FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("40px"), com.jgoodies.forms.layout.FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("357px:grow"), }, new RowSpec[] { RowSpec.decode("21px"),
				com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_ROWSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,
				com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_ROWSPEC, }));

		JLabel nomeLabel = new JLabel("Nome:");
		informacoesPanel.add(nomeLabel, "2, 1, left, center");

		idTextField = new JTextField();
		idTextField.setVisible(false);

		nomeTextField = new JTextField();
		nomeTextField.setColumns(40);
		nomeTextField.setToolTipText("Nome artístico (completo)");
		nomeTextField.setHorizontalAlignment(SwingConstants.LEFT);

		informacoesPanel.add(nomeTextField, "4, 1, 3, 1, left, top");

		JLabel tipoLabel = new JLabel("Tipo:");
		informacoesPanel.add(tipoLabel, "2, 3, left, center");

		tipoComboBox = new JComboBox<>();
		tipoComboBox.setToolTipText("Tipo de artista");
		tipoComboBox.setModel(new DefaultComboBoxModel<>(TipoArtista.values()));
		informacoesPanel.add(tipoComboBox, "4, 3, left, default");

		JPanel botoesPanel = new JPanel();
		panel.add(botoesPanel);

		consultarButton = ButtonFactory.createJButton("Consultar",
				"Procura por artistas que atendam aos critérios informados acima");
		botoesPanel.add(consultarButton);

		salvarButton = ButtonFactory.createJButton("Salvar", "Salva o artista selecionado");
		botoesPanel.add(salvarButton);

		removerButton = ButtonFactory.createJButton("Excluir", "Exclui o artista selecionado");
		removerButton.setEnabled(false);
		botoesPanel.add(removerButton);

		artistasTableModel = new ArtistaTableModel();
		artistasTable = new JTable(artistasTableModel);
		artistasTable.setAutoCreateRowSorter(true);
		artistasTable.setFillsViewportHeight(true);
		artistasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane tabelaScrollPane = new JScrollPane();
		tabelaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tabelaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		tabelaScrollPane.setViewportView(artistasTable);
		panel.add(tabelaScrollPane);

		nomeTextField.requestFocusInWindow();

		limparTela();
	}

	private void initComponents() {

	}

	/**
	 * Obtém o artista atualmente selecionado na tabela.
	 * 
	 * @return o artista selecionado
	 */
	public Optional<Artista> getArtistaSelecionado() {
		int selectedRow = artistasTable.getSelectedRow();
		if (selectedRow >= 0) {
			int modelIndex = artistasTable.convertRowIndexToModel(selectedRow);
			return modelIndex != -1 ? Optional.of(artistasTableModel.getElement(modelIndex)) : Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Obtém o ID do artista atualmente selecionad na tela.
	 * 
	 * @return o ID do artista (em branco caso nenhum artista esteja selecionado)
	 */
	public String getIdArtista() {
		return idTextField.getText();
	}

	/**
	 * Obtém o nome do artista informado na tela.
	 * 
	 * @return o nome do artista (pode estar em branco)
	 */
	public String getNomeArtista() {
		return nomeTextField.getText();
	}

	/**
	 * Obtém o tipo de artista informado na tela.
	 * 
	 * @return o tipo de artista
	 */
	public TipoArtista getTipoArtista() {
		return (TipoArtista) tipoComboBox.getSelectedItem();
	}

	/**
	 * Define os artistas sendo listados na tela.
	 * 
	 * @param artistas
	 *            os artistas
	 */
	public void setArtistas(Collection<Artista> artistas) {
		SwingUtils.invokeLater(() -> artistasTableModel.setElements(artistas));
	}

	/**
	 * Define o estado atual da tela.
	 * 
	 * @param estadoAtual
	 *            o estado
	 */
	public void setEstadoAtual(EstadoTelaCrud estadoAtual) {
		switch (estadoAtual) {
		case CONSULTA:
			nomeTextField.setEditable(true);
			tipoComboBox.setEnabled(false);
			salvarButton.setEnabled(false);
			removerButton.setEnabled(false);
			break;
		case INCLUSAO_OU_ALTERACAO:
			nomeTextField.setEditable(false);
			tipoComboBox.setEnabled(true);
			salvarButton.setEnabled(true);
			removerButton.setEnabled(true);
			break;
		}
	}

	/**
	 * Limpa todos os dados da tela.
	 */
	public void limparTela() {
		SwingUtils.clearAllData(panel);
		setEstadoAtual(EstadoTelaCrud.CONSULTA);
	}

	/**
	 * Obtém o {@link Container} utilizado para representar a tela.
	 * 
	 * @return a instância do contêiner AWT
	 */
	public Container getContainer() {
		return panel;
	}

	public JTextField getIdTextField() {
		return idTextField;
	}

	public JTextField getNomeTextField() {
		return nomeTextField;
	}

	public JComboBox<TipoArtista> getTipoComboBox() {
		return tipoComboBox;
	}

	public JTable getArtistasTable() {
		return artistasTable;
	}

	public JButton getConsultarButton() {
		return consultarButton;
	}

	public JButton getRemoverButton() {
		return removerButton;
	}

	public JButton getSalvarButton() {
		return salvarButton;
	}
}
