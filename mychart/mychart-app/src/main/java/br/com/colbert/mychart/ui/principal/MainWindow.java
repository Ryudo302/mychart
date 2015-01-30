package br.com.colbert.mychart.ui.principal;

import java.awt.*;
import java.awt.event.WindowListener;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;
import org.slf4j.Logger;

import br.com.colbert.mychart.aplicacao.principal.MainPresenter;
import br.com.colbert.mychart.infraestrutura.info.TituloAplicacao;
import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;
import br.com.colbert.mychart.ui.artista.ArtistaPanel;
import br.com.colbert.mychart.ui.inicio.InicioPanel;
import br.com.colbert.mychart.ui.topmusical.TopMusicalPanel;

/**
 * A tela principal da aplicação implementada como um {@link JFrame}.
 *
 * @author Thiago Colbert
 * @since 17/12/2014
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = MainPresenter.class)
public class MainWindow implements Serializable {

	public static final String TELA_INICIAL = "inicio";
	public static final String TELA_ARTISTAS = "artistas";
	public static final String TELA_TOP_PRINCIPAL = "topPrincipal";

	private static final long serialVersionUID = 581637404111512993L;

	@Action(name = "sair", EventType = WindowListener.class, EventAction = "windowClosing")
	private JFrame frame;

	@Inject
	private Logger logger;

	@Inject
	@TituloAplicacao
	private String tituloAplicacao;

	@Inject
	private InicioPanel inicioPanel;
	@Inject
	private ArtistaPanel artistaView;
	@Inject
	private TopMusicalPanel topMusicalView;

	@Action(name = "exibirTelaInicio")
	private JMenuItem menuItemIncio;
	@Action(name = "sair")
	private JMenuItem menuItemSair;
	@Action(name = "exibirTelaArtistas")
	private JMenuItem menuItemArtistas;
	@Action(name = "exibirTelaTopPrincipal")
	private JMenuItem menuItemTopPrincipal;
	@Action(name = "exibirTelaConfiguracoes")
	private JMenuItem menuItemConfig;
	@Action(name = "exibirTelaSobre")
	private JMenuItem menuItemSobre;

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
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setBounds(100, 100, 600, 350);
		frame.setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		menuItemIncio = new JMenuItem("Início");
		menuArquivo.add(menuItemIncio);

		menuItemSair = new JMenuItem("Sair");
		menuArquivo.add(menuItemSair);

		JMenu menuMusica = new JMenu("Música");
		menuBar.add(menuMusica);

		menuItemArtistas = new JMenuItem("Artistas");
		menuMusica.add(menuItemArtistas);

		JMenu menuRankings = new JMenu("Rankings");
		menuBar.add(menuRankings);

		menuItemTopPrincipal = new JMenuItem("Top Principal");
		menuRankings.add(menuItemTopPrincipal);

		menuItemConfig = new JMenuItem("Configurações");
		menuRankings.add(menuItemConfig);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuBar.add(menuAjuda);

		menuItemSobre = new JMenuItem("Sobre");
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
		contentPane.add(inicioPanel.getContainer(), TELA_INICIAL);
		contentPane.add(artistaView.getContainer(), TELA_ARTISTAS);
		contentPane.add(topMusicalView.getContainer(), TELA_TOP_PRINCIPAL);
	}

	/**
	 * Torna a janela visível.
	 */
	public void show() {
		SwingUtils.invokeLater(() -> frame.setVisible(true));
	}

	/**
	 * Fecha a janela.
	 */
	public void close() {
		SwingUtils.invokeLater(() -> frame.dispose());
	}

	/**
	 * Altera a tela sendo atualmente exibida na janela.
	 * 
	 * @param tela
	 *            a ser exibida
	 */
	public void mudarTela(String tela) {
		logger.debug("Mudando tela para: '{}'", tela);
		Container contentPane = frame.getContentPane();
		CardLayout layout = (CardLayout) contentPane.getLayout();
		layout.show(contentPane, tela);
	}

	/**
	 * Obtém a instância de {@link Frame} utilizada para representar a visão.
	 * 
	 * @return a instância da janela AWT
	 */
	public Frame getFrame() {
		return frame;
	}

	public JMenuItem getMenuItemArtistas() {
		return menuItemArtistas;
	}

	public JMenuItem getMenuItemConfig() {
		return menuItemConfig;
	}

	public JMenuItem getMenuItemIncio() {
		return menuItemIncio;
	}

	public JMenuItem getMenuItemSair() {
		return menuItemSair;
	}

	public JMenuItem getMenuItemSobre() {
		return menuItemSobre;
	}

	public JMenuItem getMenuItemTopPrincipal() {
		return menuItemTopPrincipal;
	}

	public static void main(String[] args) {
		new MainWindow().initFrame();
	}
}
