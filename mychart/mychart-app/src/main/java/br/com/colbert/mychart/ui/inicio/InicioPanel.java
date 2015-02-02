package br.com.colbert.mychart.ui.inicio;

import java.awt.Container;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.swing.*;

import org.mvp4j.annotation.MVP;

import br.com.colbert.base.ui.View;
import br.com.colbert.mychart.aplicacao.inicio.InicioPresenter;
import br.com.colbert.mychart.ui.principal.PainelTelaPrincipal;

/**
 * Painel representando a tela inicial da aplicação.
 * 
 * @author Thiago Colbert
 * @since 20/12/2014
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = InicioPresenter.class)
@PainelTelaPrincipal
public class InicioPanel implements View, Serializable {

	private static final long serialVersionUID = -4417535149698487678L;

	private JPanel panel;

	private JLabel nomeAppLabel;

	public static void main(String[] args) {
		new InicioPanel().init();
	}

	@PostConstruct
	protected void init() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		nomeAppLabel = new JLabel();
		panel.add(nomeAppLabel);
	}

	/**
	 * Define o texto sendo exibido na tela.
	 * 
	 * @param texto
	 *            a ser exibido
	 */
	public void setTexto(String texto) {
		nomeAppLabel.setText(texto);
	}

	/**
	 * Obtém o {@link Container} utilizado para representar a tela.
	 * 
	 * @return a instância do contêiner AWT
	 */
	@Override
	public Container getContainer() {
		return panel;
	}
}
