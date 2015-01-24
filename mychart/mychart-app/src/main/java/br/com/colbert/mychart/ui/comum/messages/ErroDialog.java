package br.com.colbert.mychart.ui.comum.messages;

import java.awt.*;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.mvp4j.annotation.*;

import br.com.colbert.base.ui.ButtonFactory;
import br.com.colbert.mychart.aplicacao.comum.ErroPresenter;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * Caixa de diálogo que exibe informações sobre um erro.
 * 
 * @author Thiago Colbert
 * @since 24/01/2015
 */
@Singleton
@MVP(modelClass = Erro.class, presenterClass = ErroPresenter.class)
public class ErroDialog implements Serializable {

	private static final long serialVersionUID = -5614995462135888543L;

	private JDialog dialog;

	@Inject
	private MainWindow mainWindow;

	@Model(property = "mensagem")
	private JLabel descricaoErroLabel;

	@Model(property = "detalhes")
	private JTextArea detalhesTextArea;

	private JButton okButton;

	public static void main(String[] args) {
		new ErroDialog().initDialog();
	}

	/**
	 * Inicializa todos os componentes da caixa de diálogo.
	 */
	@PostConstruct
	protected void init() {
		initDialog();
		initComponents();
	}

	private void initDialog() {
		dialog = new JDialog(mainWindow != null ? mainWindow.getFrame() : null, "Erro Não Tratado", true);
		dialog.setResizable(false);
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(
				ErroDialog.class.getResource("/javax/swing/plaf/metal/icons/ocean/error.png")));
		dialog.setPreferredSize(new Dimension(756, 356));

		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

		JPanel topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setHgap(10);
		flowLayout.setVgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(topPanel);

		JLabel iconeLabel = new JLabel(new ImageIcon(
				ErroDialog.class.getResource("/javax/swing/plaf/metal/icons/ocean/error.png")));
		iconeLabel.setToolTipText("Erro");
		topPanel.add(iconeLabel);

		JPanel mensagensPanel = new JPanel();
		topPanel.add(mensagensPanel);
		mensagensPanel.setLayout(new BorderLayout(0, 2));

		JLabel sumarioErroLabel = new JLabel("Ocorreu um erro não tratado:");
		sumarioErroLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mensagensPanel.add(sumarioErroLabel, BorderLayout.NORTH);

		descricaoErroLabel = new JLabel("<Mensagem>");
		descricaoErroLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mensagensPanel.add(descricaoErroLabel, BorderLayout.SOUTH);

		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(new EmptyBorder(0, 1, 0, 0));
		FlowLayout flowLayout_1 = (FlowLayout) middlePanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(middlePanel);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
		contentPane.add(bottomPanel);
		bottomPanel.setLayout(new BorderLayout(10, 10));

		JScrollPane detalhesScrollPane = new JScrollPane();
		detalhesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		detalhesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		detalhesTextArea = new JTextArea();
		detalhesTextArea.setToolTipText("Detalhes técnicos do erro");
		detalhesTextArea.setEditable(false);
		detalhesTextArea.setLineWrap(true);
		detalhesTextArea.setVisible(false);

		detalhesScrollPane.setViewportView(detalhesTextArea);
		bottomPanel.add(detalhesScrollPane);

		JCheckBox detalhesCheckBox = new JCheckBox("Mostrar detalhes");
		detalhesCheckBox.setToolTipText("Mostrar ou ocultar os detalhes do erro");
		detalhesCheckBox
				.addItemListener(event -> {
					detalhesTextArea.setVisible(!detalhesTextArea.isVisible());
					detalhesScrollPane.setVerticalScrollBarPolicy(detalhesTextArea.isVisible() ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
							: ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
					detalhesCheckBox.setText(detalhesTextArea.isVisible() ? "Ocultar detalhes" : "Mostrar detalhes");
				});
		middlePanel.add(detalhesCheckBox);

		JPanel botoesPanel = new JPanel();
		contentPane.add(botoesPanel);

		okButton = ButtonFactory.createJButton("OK", (String) null);
		okButton.addActionListener(event -> dialog.setVisible(false));
		botoesPanel.add(okButton);

		dialog.pack();
	}

	private void initComponents() {

	}

	/**
	 * 
	 */
	public void show() {
		dialog.setVisible(true);
	}

	public JLabel getDescricaoErroLabel() {
		return descricaoErroLabel;
	}

	public JTextArea getDetalhesTextArea() {
		return detalhesTextArea;
	}

	/**
	 * Obtém o {@link Dialog} utilizado para representar a tela.
	 * 
	 * @return a instância do Dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}
}
