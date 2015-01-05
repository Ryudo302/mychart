package br.com.colbert.mychart.infraestrutura.swing.sobre;

import java.awt.*;
import java.awt.event.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import br.com.colbert.mychart.infraestrutura.info.*;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao.Formato;

/**
 * Janela que exibe informações sobre a aplicação.
 * 
 * @author Thiago Colbert
 * @since 17/12/2014
 */
public class SobreDialog extends JDialog {

	private static final long serialVersionUID = 6664156130970313272L;

	private final JPanel contentPanel;

	private JLabel imagemLabel;

	@Inject
	private Icon chartImage;

	@Inject
	@TituloAplicacao(Formato.APENAS_NOME)
	private String tituloAplicacaoSimples;

	@Inject
	@TituloAplicacao(Formato.COMPLETO)
	private String tituloAplicacaoCompleto;
	private JLabel textoLabel;

	/**
	 * Create the dialog.
	 */
	public SobreDialog() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				dispose();
			}
		});
		setBounds(100, 100, 450, 262);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JPanel imagePanel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) imagePanel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(imagePanel);
			{
				imagemLabel = new JLabel();
				imagePanel.add(imagemLabel);
			}
		}
		{
			JPanel textPanel = new JPanel();
			contentPanel.add(textPanel);
			{
				textoLabel = new JLabel();
				textPanel.add(textoLabel);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	@PostConstruct
	public void init() {
		setTitle("Sobre " + tituloAplicacaoSimples);
		imagemLabel.setIcon(chartImage);
		textoLabel.setText(tituloAplicacaoCompleto);
	}
}
