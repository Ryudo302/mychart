package br.com.colbert.mychart.ui.artista;

import java.awt.event.*;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.*;
import br.com.colbert.mychart.dominio.artista.*;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;

/**
 * Implementação de {@link ArtistaView} utilizando um {@link JPanel}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class ArtistaPanel extends JPanel implements ArtistaView {

	private static final long serialVersionUID = -7371434021781119641L;

	private static final TipoArtista TIPO_ARTISTA_PADRAO = TipoArtista.DESCONHECIDO;

	private JTextField nomeTextField;
	private JTable artistasTable;
	private JButton consultarButton;
	private JButton incluirButton;
	private JButton salvarButton;
	private JButton removerButton;

	private ArtistaTableModel artistasTableModel;
	private JComboBox<TipoArtista> tipoComboBox;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.CONSULTA)
	private Event<Artista> ouvintesConsulta;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.INSERCAO)
	private Event<Artista> ouvintesInclusao;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.ATUALIZACAO)
	private Event<Artista> ouvintesAtualizacao;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.REMOCAO)
	private Event<Artista> ouvintesRemocao;

	/**
	 * Cria um novo painel.
	 */
	public ArtistaPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel informacoesPanel = new JPanel();
		informacoesPanel.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		add(informacoesPanel);
		informacoesPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("40px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("357px:grow"), }, new RowSpec[] { RowSpec.decode("21px"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, }));

		JLabel nomeLabel = new JLabel("Nome:");
		informacoesPanel.add(nomeLabel, "2, 1, left, center");

		nomeTextField = new JTextField();
		nomeTextField.setToolTipText("Nome artístico (completo)");
		nomeTextField.setColumns(46);
		nomeTextField.setHorizontalAlignment(SwingConstants.LEFT);
		informacoesPanel.add(nomeTextField, "4, 1, left, top");

		JLabel tipoLabel = new JLabel("Tipo:");
		informacoesPanel.add(tipoLabel, "2, 3, left, center");

		tipoComboBox = new JComboBox<>();
		tipoComboBox.setToolTipText("Tipo de artista");
		tipoComboBox.setModel(new DefaultComboBoxModel<>(TipoArtista.values()));
		informacoesPanel.add(tipoComboBox, "4, 3, left, default");

		JPanel botoesPanel = new JPanel();
		add(botoesPanel);

		incluirButton = ButtonFactory.createJButton("Incluir", "Inclui um novo artista com os dados informados acima");
		incluirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ouvintesInclusao.fire(getArtistaAtual());
				limparTela();
			}
		});

		consultarButton = ButtonFactory.createJButton("Consultar", "Procura por artistas que atendam aos critérios informados acima");
		consultarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ouvintesConsulta.fire(getArtistaAtual());
			}
		});
		botoesPanel.add(consultarButton);

		botoesPanel.add(incluirButton);

		salvarButton = ButtonFactory.createJButton("Salvar", "Salva as alterações realizadas nos dados do artista");
		salvarButton.setEnabled(false);
		salvarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ouvintesAtualizacao.fire(getArtistaAtual());
				limparTela();
			}
		});
		botoesPanel.add(salvarButton);

		removerButton = ButtonFactory.createJButton("Excluir", "Exclui o artista selecionado");
		removerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ouvintesRemocao.fire(getArtistaAtual());
				limparTela();
			}
		});
		removerButton.setEnabled(false);
		botoesPanel.add(removerButton);

		JScrollPane tabelaScrollPane = new JScrollPane();
		tabelaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		add(tabelaScrollPane);

		artistasTable = new JTable();
		artistasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		artistasTableModel = new ArtistaTableModel();
		artistasTable.setModel(artistasTableModel);

		artistasTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				setEntidadeAtual(artistasTableModel.getElement(artistasTable.convertRowIndexToModel(artistasTable.getSelectedRow())));
				setEstadoAtual(EstadoTelaCrud.INCLUSAO_OU_ALTERACAO);
			}
		});

		artistasTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
					limparTela();
				}
			}
		});

		tabelaScrollPane.setViewportView(artistasTable);
	}

	@PostConstruct
	protected void init() {
		limparTela();
	}

	private Artista getArtistaAtual() {
		return new Artista(nomeTextField.getText(), (TipoArtista) tipoComboBox.getSelectedItem());
	}

	@Override
	public void setEntidadeAtual(Artista artista) {
		this.nomeTextField.setText(artista.getNome());
		this.tipoComboBox.setSelectedItem(artista.getTipo());
	}

	@Override
	public void setEntidades(Collection<Artista> artistas) {
		this.artistasTableModel.setElements(artistas);
	}

	private void setEstadoAtual(EstadoTelaCrud estadoAtual) {
		switch (estadoAtual) {
		case CONSULTA:
			this.incluirButton.setEnabled(true);
			this.salvarButton.setEnabled(false);
			this.removerButton.setEnabled(false);
			break;
		case INCLUSAO_OU_ALTERACAO:
			this.incluirButton.setEnabled(false);
			this.salvarButton.setEnabled(true);
			this.removerButton.setEnabled(true);
			break;
		}
	}

	public void limparTela() {
		setEntidadeAtual(novoArtista());
		setEstadoAtual(EstadoTelaCrud.CONSULTA);
	}

	private Artista novoArtista() {
		return new Artista(StringUtils.EMPTY, TIPO_ARTISTA_PADRAO);
	}
}
