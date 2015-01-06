package br.com.colbert.mychart.infraestrutura.swing.artista;

import java.awt.Container;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.*;
import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.ui.artista.ArtistaView;

/**
 * Implementação de {@link ArtistaView} utilizando um {@link JPanel}.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@ApplicationScoped
public class ArtistaSwingView implements ArtistaView, Serializable {

	private static final long serialVersionUID = -7371434021781119641L;

	private JPanel panel;

	private JTextField idTextField;
	private JTextField nomeTextField;
	private JComboBox<TipoArtista> tipoComboBox;

	private JTable artistasTable;
	private ArtistaTableModel artistasTableModel;

	private JButton consultarButton;
	private JButton salvarButton;
	private JButton removerButton;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.CONSULTA)
	private Event<Artista> ouvintesConsulta;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.INSERCAO)
	private Event<Artista> ouvintesInclusao;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.REMOCAO)
	private Event<Artista> ouvintesRemocao;

	@Inject
	@Any
	private Event<ArtistaView> ouvintesView;

	public static void main(String[] args) {
		new ArtistaSwingView().init();
	}

	@PostConstruct
	protected void init() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent event) {
				ouvintesView.fire(ArtistaSwingView.this);
			}
		});

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
		nomeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				consultarButton.doClick();
			}
		});
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

		salvarButton = ButtonFactory.createJButton("Salvar", "Salva o artista selecionado");
		salvarButton.addActionListener(event -> {
			ouvintesInclusao.fire(getArtistaAtual());
			limparTela();
		});

		consultarButton = ButtonFactory.createJButton("Consultar",
				"Procura por artistas que atendam aos critérios informados acima");
		consultarButton.addActionListener(event -> ouvintesConsulta.fire(getArtistaAtual()));
		botoesPanel.add(consultarButton);

		botoesPanel.add(salvarButton);

		removerButton = ButtonFactory.createJButton("Excluir", "Exclui o artista selecionado");
		removerButton.addActionListener(event -> {
			ouvintesRemocao.fire(getArtistaAtual());
			limparTela();
		});
		removerButton.setEnabled(false);
		botoesPanel.add(removerButton);

		artistasTableModel = new ArtistaTableModel();
		artistasTable = new JTable(artistasTableModel);
		artistasTable.setAutoCreateRowSorter(true);
		artistasTable.setFillsViewportHeight(true);
		artistasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		artistasTable.getSelectionModel().addListSelectionListener(event -> {
			getArtistaSelecionado().ifPresent(artista -> {
				setArtistaAtual(artista);
				setEstadoAtual(EstadoTelaCrud.INCLUSAO_OU_ALTERACAO);
			});
		});

		artistasTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				int keyCode = event.getKeyCode();
				if (keyCode == KeyEvent.VK_ESCAPE) {
					limparTela();
				} else if (keyCode == KeyEvent.VK_DELETE) {
					getArtistaSelecionado().ifPresent(artista -> ouvintesRemocao.fire(artista));
				}
			}
		});

		JScrollPane tabelaScrollPane = new JScrollPane();
		tabelaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tabelaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		tabelaScrollPane.setViewportView(artistasTable);
		panel.add(tabelaScrollPane);

		nomeTextField.requestFocusInWindow();

		limparTela();
	}

	private Artista getArtistaAtual() {
		return new Artista(idTextField.getText(), nomeTextField.getText(), (TipoArtista) tipoComboBox.getSelectedItem());
	}

	private Optional<Artista> getArtistaSelecionado() {
		int selectedRow = artistasTable.getSelectedRow();
		if (selectedRow >= 0) {
			int modelIndex = artistasTable.convertRowIndexToModel(selectedRow);
			return modelIndex != -1 ? Optional.of(artistasTableModel.getElement(modelIndex)) : Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void setArtistaAtual(Artista artista) {
		idTextField.setText(artista.getId());
		nomeTextField.setText(artista.getNome());
		tipoComboBox.setSelectedItem(artista.getTipo());
	}

	@Override
	public void setArtistas(Collection<Artista> artistas) {
		artistasTableModel.setElements(artistas);
	}

	private void setEstadoAtual(EstadoTelaCrud estadoAtual) {
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

	public void limparTela() {
		setArtistaAtual(novoArtista());
		setEstadoAtual(EstadoTelaCrud.CONSULTA);
	}

	private Artista novoArtista() {
		return Artista.ARTISTA_NULL;
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
