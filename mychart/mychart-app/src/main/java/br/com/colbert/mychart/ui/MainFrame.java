package br.com.colbert.mychart.ui;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import br.com.colbert.mychart.infraestrutura.eventos.app.*;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao;
import br.com.colbert.mychart.ui.comum.sobre.SobreDialog;

/**
 * A tela principal da aplicação implementada como um {@link JFrame}.
 * 
 * @author Thiago Colbert
 * @since 17/12/2014
 */
public class MainFrame extends JFrame implements MainWindow {

	private static final long serialVersionUID = 581637404111512993L;

	private static final String COMANDO_SAIR = "sair";

	@Inject
	@TituloAplicacao
	private String tituloAplicacao;

	private final Action sairAction;

	@Inject
	private SobreDialog sobreDialog;

	private final JPanel contentPane;

	@Inject
	@StatusAplicacao(TipoStatusAplicacao.ENCERRADA)
	private Event<MainWindow> ouvintesEncerramento;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		sairAction = new SairAction();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				sairAction.actionPerformed(new ActionEvent(event.getSource(), event.getID(), COMANDO_SAIR));
			}
		});

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		JMenuItem menuItemSair = new JMenuItem("Sair");
		menuItemSair.setAction(sairAction);
		menuArquivo.add(menuItemSair);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuBar.add(menuAjuda);

		JMenuItem menuItemSobre = new JMenuItem("Sobre");
		menuItemSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sobreDialog.setVisible(true);
			}
		});
		menuAjuda.add(menuItemSobre);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		setContentPane(contentPane);
	}

	@PostConstruct
	public void init() {
		setTitle(tituloAplicacao);
	}

	private class SairAction extends AbstractAction {

		private static final long serialVersionUID = -5817088900948179469L;

		public SairAction() {
			putValue(ACTION_COMMAND_KEY, COMANDO_SAIR);
			putValue(NAME, "Sair");
			putValue(SHORT_DESCRIPTION, "Fecha a aplicação.");
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			ouvintesEncerramento.fire(MainFrame.this);
		}
	}
}
