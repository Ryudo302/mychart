package br.com.colbert.mychart.infraestrutura.swing.comum;

import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.*;

import br.com.colbert.mychart.infraestrutura.providers.ImagesProvider;
import br.com.colbert.mychart.infraestrutura.swing.SwingUtils;
import br.com.colbert.mychart.infraestrutura.swing.principal.SwingMainWindow;
import br.com.colbert.mychart.ui.comum.LoadingView;

/**
 * Implementação de {@link LoadingView} utilizando Swing.
 * 
 * @author Thiago Colbert
 * @since 09/01/2015
 */
@ApplicationScoped
public class LoadingSwingView implements LoadingView, Serializable {

	private static final long serialVersionUID = 8248885596292025385L;

	private JDialog dialog;

	@Inject
	private SwingMainWindow mainWindow;

	@Inject
	private ImagesProvider images;

	public static void main(String[] args) throws IOException {
		new LoadingSwingView().init();
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
					hide();
				}
			}
		});

		JLabel imagemLabel = new JLabel();
		imagemLabel.setIcon(Beans.isDesignTime() ? null : images.loading());
		dialog.getContentPane().add(imagemLabel, BorderLayout.CENTER);

		dialog.pack();
	}

	@Override
	public void show() {
		SwingUtils.invokeLater(() -> dialog.setVisible(true));
	}

	@Override
	public void hide() {
		SwingUtils.invokeLater(() -> dialog.setVisible(false));
	}

	public Window getWindow() {
		return dialog;
	}
}
