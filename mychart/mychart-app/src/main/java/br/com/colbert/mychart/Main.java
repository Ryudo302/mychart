package br.com.colbert.mychart;

import br.com.colbert.mychart.aplicacao.MainController;

/**
 * Classe principal da aplicação, responsável pelo <em>bootstrap</em>.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class Main {

	public static void main(String[] args) {
		new MainController().iniciar();
	}
}
