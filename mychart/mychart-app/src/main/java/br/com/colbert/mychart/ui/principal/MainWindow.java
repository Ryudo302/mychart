package br.com.colbert.mychart.ui.principal;

import java.awt.*;
import java.awt.event.WindowListener;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;

import br.com.colbert.base.ui.WindowView;
import br.com.colbert.mychart.aplicacao.principal.MainPresenter;
import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;

/**
 * A tela principal da aplicação implementada como um {@link JFrame}.
 *
 * @author Thiago Colbert
 * @since 17/12/2014
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = MainPresenter.class)
public class MainWindow implements WindowView, Serializable {

	private static final long serialVersionUID = 581637404111512993L;

	@Action(name = "sair", EventType = WindowListener.class, EventAction = "windowClosing")
	private JFrame frame;

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

	@Override
	public void close() {
		// sobrescreve para chamar dispose() ao invés de setVisible(false)
		SwingUtils.invokeLater(() -> frame.dispose());
	}

	/**
	 * Altera a tela sendo atualmente exibida na janela.
	 * 
	 * @param tela
	 *            a ser exibida
	 */
	public void mudarTela(String tela) {
		Container contentPane = frame.getContentPane();
		CardLayout layout = (CardLayout) contentPane.getLayout();
		layout.show(contentPane, tela);
	}

	@Override
	public Container getContainer() {
		return getFrame();
	}

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
		new MainWindow().init();
	}
}
