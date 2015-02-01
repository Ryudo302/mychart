package br.com.colbert.mychart.ui.topmusical;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.ButtonFactory;
import br.com.colbert.mychart.aplicacao.topmusical.TopMusicalConfigPresenter;
import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.infraestrutura.providers.ImagesProvider;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * Tela de {@link TopMusicalConfiguration}.
 *
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@Singleton
@MVP(modelClass = TopMusicalConfiguration.class, presenterClass = TopMusicalConfigPresenter.class)
public class TopMusicalConfigDialog implements Serializable {

	private static final long serialVersionUID = -3162266008710325966L;

	@Inject
	private ImagesProvider images;

	@Inject
	private MainWindow mainWindow;
	@Action(name = "descartarAlteracoes", EventAction = "windowClosing", EventType = WindowListener.class)
	private JDialog dialog;

	@Model(property = "quantidadePosicoes", initProperty = "quantidadesPosicoesPermitidas")
	@Action(name = "quantidadePosicoesSelecionada", EventAction = "stateChanged", EventType = ChangeListener.class)
	private JSpinner quantidadePosicoesSpinner;
	@Model(property = "frequencia", initProperty = "frequenciasPermitidas")
	@Action(name = "frequenciaSelecionada", EventAction = "itemStateChanged", EventType = ItemListener.class)
	private JComboBox<Frequencia> frequenciaComboBox;

	@Action(name = "salvarAlteracoes")
	private JButton salvarButton;
	@Action(name = "descartarAlteracoes")
	private JButton cancelarButton;

	public static void main(String[] args) {
		new TopMusicalConfigDialog().initDialog();
	}

	/**
	 * Inicializa todos os componentes da tela.
	 */
	@PostConstruct
	protected void init() {
		initDialog();
		initComponents();
	}

	private void initDialog() {
		dialog = new JDialog(mainWindow.getFrame(), true);
		dialog.setLocationRelativeTo(mainWindow.getFrame());
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		Container panel = dialog.getContentPane();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel frequenciaLabel = new JLabel("Frequência:");
		infoPanel.add(frequenciaLabel, "2, 2, left, center");

		frequenciaComboBox = new JComboBox<Frequencia>();
		frequenciaComboBox.setModel(new DefaultComboBoxModel<>(TopMusicalConfiguration.FREQUENCIAS_PERMITIDAS));
		infoPanel.add(frequenciaComboBox, "4, 2, fill, default");

		JLabel quantidadePosicoesLabel = new JLabel("Quantidade de Posições:");
		infoPanel.add(quantidadePosicoesLabel, "2, 4, left, center");

		quantidadePosicoesSpinner = new JSpinner();
		JComponent comp = quantidadePosicoesSpinner.getEditor();
		JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
		DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
		formatter.setCommitsOnValidEdit(true);
		infoPanel.add(quantidadePosicoesSpinner, "4, 4");

		JPanel botoesPanel = new JPanel();

		salvarButton = ButtonFactory.createJButton("Salvar", "Salva as configurações.");
		botoesPanel.add(salvarButton);

		cancelarButton = ButtonFactory.createJButton("Cancelar", "Cancela as alterações feitas");
		botoesPanel.add(cancelarButton);

		panel.add(infoPanel);
		panel.add(botoesPanel);

		dialog.pack();
	}

	private void initComponents() {
		salvarButton.setIcon(images.loadImageAsIcon("save.png"));
		cancelarButton.setIcon(images.loadImageAsIcon("remove.png"));
	}

	public Integer getQuantidadePosicoes() {
		return (Integer) quantidadePosicoesSpinner.getValue();
	}

	public Frequencia getFrequencia() {
		return (Frequencia) frequenciaComboBox.getSelectedItem();
	}

	public void show() {
		dialog.setVisible(true);
	}

	public void close() {
		dialog.setVisible(false);
	}

	/**
	 * Obtém o {@link Dialog} utilizado para representar a tela.
	 *
	 * @return a instância do contêiner AWT
	 */
	public Dialog getDialog() {
		return dialog;
	}
}
