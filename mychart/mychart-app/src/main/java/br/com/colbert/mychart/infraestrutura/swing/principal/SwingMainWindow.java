package br.com.colbert.mychart.infraestrutura.swing.principal;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;

import br.com.colbert.mychart.infraestrutura.eventos.app.*;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao;
import br.com.colbert.mychart.infraestrutura.swing.artista.ArtistaSwingView;
import br.com.colbert.mychart.infraestrutura.swing.home.HomeSwingView;
import br.com.colbert.mychart.infraestrutura.swing.sobre.SobreDialog;
import br.com.colbert.mychart.infraestrutura.swing.topmusical.*;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * A tela principal da aplicação implementada como um {@link JFrame}.
 *
 * @author Thiago Colbert
 * @since 17/12/2014
 */
@ApplicationScoped
public class SwingMainWindow implements MainWindow, Serializable {

	private static final long serialVersionUID = 581637404111512993L;

	private static final String TELA_INICIAL = "inicio";
	private static final String TELA_ARTISTAS = "artistas";
	private static final String TELA_TOP_PRINCIPAL = "topPrincipal";

	private static final String COMANDO_SAIR = "sair";

	private JFrame frame;

	private Action sairAction;

	@Inject
	private Logger logger;

	@Inject
	@TituloAplicacao
	private String tituloAplicacao;

	@Inject
	private HomeSwingView homeSwingView;
	@Inject
	private ArtistaSwingView artistaView;
	@Inject
	private TopMusicalSwingView topMusicalView;

	@Inject
	private TopMusicalConfigSwingView topMusicalConfigView;
	@Inject
	private SobreDialog sobreDialog;

	@Inject
	@StatusAplicacao(TipoStatusAplicacao.ENCERRADA)
	private Event<MainWindow> ouvintesEncerramento;

	public static void main(String[] args) {
		new SwingMainWindow().initFrame();
	}

	/**
	 * Inicializa todos os componentes da janela.
	 */
	@PostConstruct
	protected void init() {
		initFrame();
		initComponents();
	}

	/**
	 * Inicializa os componentes da janela que não dependem do CDI.
	 */
	protected void initFrame() {
		frame = new JFrame();
		sairAction = new SairAction();

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				logger.debug("Fechando janela");
				sairAction.actionPerformed(new ActionEvent(event.getSource(), event.getID(), SwingMainWindow.COMANDO_SAIR));
			}
		});

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setBounds(100, 100, 600, 350);
		frame.setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		JMenuItem menuItemIncio = new JMenuItem("Início");
		menuItemIncio.addActionListener(event -> mudarTela(TELA_INICIAL));
		menuArquivo.add(menuItemIncio);

		JMenuItem menuItemSair = new JMenuItem("Sair");
		menuItemSair.setAction(sairAction);
		menuArquivo.add(menuItemSair);

		JMenu menuMusica = new JMenu("Música");
		menuBar.add(menuMusica);

		JMenuItem menuItemArtistas = new JMenuItem("Artistas");
		menuItemArtistas.addActionListener(event -> {
			logger.debug("Exibindo painel de artistas");
			mudarTela(TELA_ARTISTAS);
		});
		menuMusica.add(menuItemArtistas);

		JMenu menuRankings = new JMenu("Rankings");
		menuBar.add(menuRankings);

		JMenuItem menuItemTopPrincipal = new JMenuItem("Top Principal");
		menuItemTopPrincipal.addActionListener(event -> {
			logger.debug("Exibindo painel do top principal");
			mudarTela(TELA_TOP_PRINCIPAL);
		});
		menuRankings.add(menuItemTopPrincipal);

		JMenuItem menuItemConfig = new JMenuItem("Configurações");
		menuItemConfig.addActionListener(event -> {
			topMusicalConfigView.show();
		});
		menuRankings.add(menuItemConfig);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuBar.add(menuAjuda);

		JMenuItem menuItemSobre = new JMenuItem("Sobre");
		menuItemSobre.addActionListener(event -> {
			logger.debug("Exibindo janela 'Sobre'");
			sobreDialog.setVisible(true);
		});
		menuAjuda.add(menuItemSobre);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new CardLayout(0, 0));

		frame.setContentPane(contentPane);
	}

	/**
	 * Inicializa os componentes da janela injetados pelo CDI.
	 */
	protected void initComponents() {
		frame.setTitle(tituloAplicacao);

		Container contentPane = frame.getContentPane();
		contentPane.add(homeSwingView.getContainer(), TELA_INICIAL);
		contentPane.add(artistaView.getContainer(), TELA_ARTISTAS);
		contentPane.add(topMusicalView.getContainer(), TELA_TOP_PRINCIPAL);
	}

	@Override
	public void show() {
		frame.setVisible(true);
	}

	@Override
	public void close() {
		frame.dispose();
	}

	private void mudarTela(String tela) {
		logger.debug("Mudando tela para: '{}'", tela);
		Container contentPane = frame.getContentPane();
		CardLayout layout = (CardLayout) contentPane.getLayout();
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
			ouvintesEncerramento.fire(SwingMainWindow.this);
		}
	}

	/**
	 * Obtém a instância de {@link Frame} utilizada para representar a visão.
	 * 
	 * @return a instância da janela AWT
	 */
	public Frame getFrame() {
		return frame;
	}
}
