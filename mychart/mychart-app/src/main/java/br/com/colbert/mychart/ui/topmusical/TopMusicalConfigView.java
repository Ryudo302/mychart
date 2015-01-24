package br.com.colbert.mychart.ui.topmusical;

import java.awt.Container;
import java.awt.event.*;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.text.DefaultFormatter;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import com.jgoodies.forms.layout.*;

import br.com.colbert.base.ui.ButtonFactory;
import br.com.colbert.mychart.dominio.topmusical.*;
import br.com.colbert.mychart.ui.comum.messages.MessagesView;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * Tela de {@link TopMusicalConfiguration}.
 *
 * @author Thiago Colbert
 * @since 04/01/2015
 */
@ApplicationScoped
public class TopMusicalConfigView implements Serializable {

	private static final long serialVersionUID = -3162266008710325966L;

	@Inject
	private Logger logger;

	@Inject
	private TopMusicalConfiguration topMusicalConfiguration;

	@Inject
	private MainWindow mainWindow;
	@Inject
	private MessagesView messagesView;

	private JDialog dialog;

	private JSpinner quantidadePosicoesSpinner;
	private JComboBox<Frequencia> frequenciaComboBox;

	public static void main(String[] args) {
		new TopMusicalConfigView().initDialog();
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
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				descartarAlteracoes();
				close();
			}
		});
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
		frequenciaComboBox.addItemListener(event -> {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				topMusicalConfiguration.setFrequencia((Frequencia) frequenciaComboBox.getSelectedItem());
			}
		});
		frequenciaComboBox.setModel(new DefaultComboBoxModel<Frequencia>(ArrayUtils
				.toArray(Frequencia.DIARIO, Frequencia.SEMANAL)));
		infoPanel.add(frequenciaComboBox, "4, 2, fill, default");

		JLabel quantidadePosicoesLabel = new JLabel("Quantidade de Posições:");
		infoPanel.add(quantidadePosicoesLabel, "2, 4, left, center");

		quantidadePosicoesSpinner = new JSpinner();
		JComponent comp = quantidadePosicoesSpinner.getEditor();
		JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
		DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
		formatter.setCommitsOnValidEdit(true);

		quantidadePosicoesSpinner.addChangeListener(event -> {
			topMusicalConfiguration.setQuantidadePosicoes((Integer) quantidadePosicoesSpinner.getValue());
		});
		quantidadePosicoesSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		infoPanel.add(quantidadePosicoesSpinner, "4, 4");

		JPanel botoesPanel = new JPanel();

		JButton salvarButton = ButtonFactory.createJButton("Salvar", "Salva as configurações.");
		salvarButton.addActionListener(event -> {
			salvarAlteracoes();
			close();
		});
		botoesPanel.add(salvarButton);

		JButton cancelarButton = ButtonFactory.createJButton("Cancelar", "Cancela as alterações feitas");
		cancelarButton.addActionListener(event -> {
			descartarAlteracoes();
			close();
		});
		botoesPanel.add(cancelarButton);

		panel.add(infoPanel);
		panel.add(botoesPanel);

		dialog.pack();
	}

	private void initComponents() {
		frequenciaComboBox.setSelectedItem(topMusicalConfiguration.getFrequencia());
		quantidadePosicoesSpinner.setValue(topMusicalConfiguration.getQuantidadePosicoes());
	}

	private void salvarAlteracoes() {
		try {
			topMusicalConfiguration.salvar();
		} catch (IOException exception) {
			logger.error("Erro ao salvar configurações", exception);
			messagesView.adicionarMensagemErro("Erro ao salvar configurações", exception);
		}
	}

	private void descartarAlteracoes() {
		try {
			topMusicalConfiguration.descartarAlteracoes();
		} catch (Exception exception) {
			logger.warn("Erro ao desfazer alterações", exception);
		}
	}

	public void show() {
		dialog.setVisible(true);
	}

	public void close() {
		dialog.setVisible(false);
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
