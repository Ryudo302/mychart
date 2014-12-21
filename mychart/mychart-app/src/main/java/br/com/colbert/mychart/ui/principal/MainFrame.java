package br.com.colbert.mychart.ui.principal;

import java.awt.*;
import java.awt.event.*;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;

import br.com.colbert.mychart.infraestrutura.eventos.app.*;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao;
import br.com.colbert.mychart.ui.artista.ArtistaPanel;
import br.com.colbert.mychart.ui.comum.sobre.SobreDialog;

/**
 * A tela principal da aplicação implementada como um {@link JFrame}.
 * 
 * @author Thiago Colbert
 * @since 17/12/2014
 */
public class MainFrame extends JFrame implements MainWindow {

	private static final long serialVersionUID = 581637404111512993L;

	private static final String TELA_INICIAL = "inicio";
	private static final String TELA_ARTISTAS = "artistas";

	private static final String COMANDO_SAIR = "sair";

	private Action sairAction;

	@Inject
	private Logger logger;

	@Inject
	@TituloAplicacao
	private String tituloAplicacao;

	@Inject
	private InicioPanel inicioPanel;
	@Inject
	private ArtistaPanel artistaPanel;

	@Inject
	private SobreDialog sobreDialog;

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
				logger.debug("Fechando janela");
				sairAction.actionPerformed(new ActionEvent(event.getSource(), event.getID(), MainFrame.COMANDO_SAIR));
			}
		});

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		JMenuItem menuItemIncio = new JMenuItem("Início");
		menuItemIncio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mudarTela(TELA_INICIAL);
			}
		});
		menuArquivo.add(menuItemIncio);

		JMenuItem menuItemSair = new JMenuItem("Sair");
		menuItemSair.setAction(sairAction);
		menuArquivo.add(menuItemSair);

		JMenu menuMusica = new JMenu("Música");
		menuBar.add(menuMusica);

		JMenuItem menuItemArtistas = new JMenuItem("Artistas");
		menuItemArtistas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				logger.debug("Exibindo painel de artistas");
				mudarTela(TELA_ARTISTAS);
			}
		});
		menuMusica.add(menuItemArtistas);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuBar.add(menuAjuda);

		JMenuItem menuItemSobre = new JMenuItem("Sobre");
		menuItemSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				logger.debug("Exibindo janela 'Sobre'");
				sobreDialog.setVisible(true);
			}
		});
		menuAjuda.add(menuItemSobre);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new CardLayout(0, 0));

		setContentPane(contentPane);
	}

	@PostConstruct
	protected void initComponents() {
		setTitle(tituloAplicacao);

		Container contentPane = getContentPane();
		contentPane.add(inicioPanel, TELA_INICIAL);
		contentPane.add(artistaPanel, TELA_ARTISTAS);
	}

	@Override
	public void close() {
		dispose();
	}

	private void mudarTela(String tela) {
		logger.debug("Mudando tela para: '{}'", tela);
		Container contentPane = getContentPane();
		CardLayout layout = ((CardLayout) contentPane.getLayout());
		layout.show(contentPane, tela);
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
			logger.debug("Disparando evento de '{}'", COMANDO_SAIR);
			ouvintesEncerramento.fire(MainFrame.this);
		}
	}
}
