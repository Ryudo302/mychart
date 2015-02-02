package br.com.colbert.mychart.ui.comum;

import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.swing.*;

import br.com.colbert.base.ui.WindowView;
import br.com.colbert.mychart.infraestrutura.providers.ImagesProvider;
import br.com.colbert.mychart.infraestrutura.swing.worker.LoadingView;
import br.com.colbert.mychart.ui.principal.MainWindow;

/**
 * Tela exibida quando é aguardada a execução de uma tarefa em <em>background</em>.
 * 
 * @author Thiago Colbert
 * @since 09/01/2015
 */
@ApplicationScoped
@LoadingView
public class LoadingDialog implements WindowView, Serializable {

	private static final long serialVersionUID = 8248885596292025385L;

	private JDialog dialog;

	@Inject
	private MainWindow mainWindow;
	@Inject
	private ImagesProvider images;

	public static void main(String[] args) throws IOException {
		new LoadingDialog().init();
	}

	@PostConstruct
	protected void init() throws IOException {
		dialog = new JDialog(mainWindow != null ? mainWindow.getFrame() : null, true);
		dialog.setUndecorated(true);
		dialog.setPreferredSize(new Dimension(200, 230));
		dialog.setResizable(false);
		dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dialog.setLocationRelativeTo(mainWindow != null ? mainWindow.getFrame() : null);

		dialog.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dialog.setVisible(false);
				}
			}
		});

		JLabel imagemLabel = new JLabel();
		imagemLabel.setIcon(Beans.isDesignTime() ? null : images.loading());
		dialog.getContentPane().add(imagemLabel, BorderLayout.CENTER);

		dialog.pack();
	}

	/**
	 * 
	 * @return
	 */
	@Produces
	@LoadingView
	public Window getWindow() {
		return dialog;
	}

	@Override
	public Container getContainer() {
		return getWindow();
	}
}
