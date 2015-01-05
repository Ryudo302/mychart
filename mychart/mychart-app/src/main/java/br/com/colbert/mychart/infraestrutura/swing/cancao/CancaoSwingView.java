package br.com.colbert.mychart.infraestrutura.swing.cancao;

import java.awt.Container;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.ButtonFactory;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.eventos.crud.*;
import br.com.colbert.mychart.infraestrutura.swing.principal.SwingMainWindow;
import br.com.colbert.mychart.ui.cancao.CancaoView;
import br.com.colbert.mychart.ui.comum.CausaSaidaDeView;

/**
 * Implementação de {@link CancaoView} utilizando Swing.
 * 
 * @author Thiago Colbert
 * @since 05/01/2015
 */
public class CancaoSwingView implements CancaoView, Serializable {

	private static final long serialVersionUID = 8909053142047968045L;

	private JDialog dialog;
	private CausaSaidaDeView causaFechamento;
	private Cancao cancaoAtual;

	private JList<Cancao> cancoesList;

	private JTextField tituloTextField;
	private JButton consultarButton;
	private JButton salvarButton;

	@Inject
	private SwingMainWindow mainWindow;

	@Inject
	@OperacaoCrud(TipoOperacaoCrud.CONSULTA)
	private Event<Cancao> ouvintesConsulta;

	public static void main(String[] args) {
		new CancaoSwingView().initPanel();
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
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {
				causaFechamento = CausaSaidaDeView.CANCELAMENTO;
				close();
			}
		});

		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel informacoesPanel = new JPanel();
		informacoesPanel.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es", TitledBorder.LEFT, TitledBorder.TOP, null,
				null));
		informacoesPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("3dlu:grow"), FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormSpecs.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("20px"), }));

		JLabel tituloLabel = new JLabel("Título:");
		informacoesPanel.add(tituloLabel, "2, 2, right, center");

		contentPane.add(informacoesPanel);

		tituloTextField = new JTextField();
		informacoesPanel.add(tituloTextField, "4, 2, fill, default");

		JPanel botoesPanel = new JPanel();

		consultarButton = ButtonFactory.createJButton("Consultar",
				"Procura por artistas que atendam aos critérios informados acima");
		consultarButton.addActionListener(event -> ouvintesConsulta.fire(getCancaoAtual().get()));
		botoesPanel.add(consultarButton);

		contentPane.add(botoesPanel);

		salvarButton = ButtonFactory.createJButton("Salvar", "Salva o artista selecionado");
		salvarButton.addActionListener(event -> {
			causaFechamento = CausaSaidaDeView.CONFIRMACAO;
			close();
		});
		botoesPanel.add(salvarButton);

		CancoesListModel cancoesListModel = new CancoesListModel();
		cancoesList = new JList<>(cancoesListModel);
		cancoesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane cancoesListScrollPane = new JScrollPane();
		cancoesListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		cancoesListScrollPane.setViewportView(cancoesList);
		contentPane.add(cancoesListScrollPane);

		dialog.pack();
	}

	private void initComponents() {
		cancaoAtual = Cancao.CANCAO_NULL;
	}

	@Override
	public CausaSaidaDeView show() {
		dialog.setVisible(true);
		return causaFechamento;
	}

	@Override
	public void close() {
		dialog.setVisible(false);
	}

	@Override
	public List<Cancao> getCancoesSelecionadas() {
		return cancoesList.getSelectedValuesList();
	}

	@Override
	public void setCancoesDisponiveis(Collection<Cancao> cancoes) {
		// TODO não atualizando lista na tela
		((CancoesListModel) cancoesList.getModel()).setElements(cancoes);
	}

	@Override
	public Optional<Cancao> getCancaoAtual() {
		cancaoAtual.setTitulo(tituloTextField.getText());
		return Optional.of(cancaoAtual);
	}

	public void setCancaoAtual(Cancao cancao) {
		this.cancaoAtual = cancao;
		this.tituloTextField.setText(cancao.getTitulo());
	}

	/**
	 * Obtém o {@link Container} utilizado para representar a tela.
	 * 
	 * @return a instância do contêiner AWT
	 */
	public Container getContainer() {
		return dialog;
	}
}
