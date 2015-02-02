package br.com.colbert.mychart.ui.sobre;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import br.com.colbert.base.ui.WindowView;
import br.com.colbert.mychart.infraestrutura.info.*;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao.Formato;
import br.com.colbert.mychart.infraestrutura.providers.ImagesProvider;

/**
 * Janela que exibe informações sobre a aplicação.
 * 
 * @author Thiago Colbert
 * @since 17/12/2014
 */
public class SobreDialog implements WindowView, Serializable {

	private static final long serialVersionUID = 6664156130970313272L;

	private JDialog dialog;

	private JLabel imagemLabel;
	private JLabel textoLabel;

	@Inject
	private ImagesProvider images;

	@Inject
	@TituloAplicacao(Formato.APENAS_NOME)
	private String tituloAplicacaoSimples;

	@Inject
	@TituloAplicacao(Formato.COMPLETO)
	private String tituloAplicacaoCompleto;

	/**
	 * Create the dialog.
	 */
	public SobreDialog() {
		dialog = new JDialog();
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {
				dialog.dispose();
			}
		});

		dialog.setBounds(100, 100, 450, 262);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.getContentPane().setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
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
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			contentPanel.add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						dialog.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				dialog.getRootPane().setDefaultButton(okButton);
			}
		}
	}

	public static void main(String[] args) {
		new SobreDialog();
	}

	@PostConstruct
	public void init() throws IOException {
		dialog.setTitle("Sobre " + tituloAplicacaoSimples);
		imagemLabel.setIcon(images.chart());
		textoLabel.setText(tituloAplicacaoCompleto);
	}

	@Override
	public Container getContainer() {
		return dialog;
	}
}
