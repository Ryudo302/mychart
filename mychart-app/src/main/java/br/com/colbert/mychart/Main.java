package br.com.colbert.mychart;

import javax.validation.Validator;

import br.com.colbert.mychart.infraestrutura.CdiUtils;

/**
 * Classe principal da aplicação, responsável pelo <em>bootstrap</em>.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class Main {

	public static void main(String[] args) {
		try {
			CdiUtils.init();
			CdiUtils.getBean(Validator.class);
		} finally {
			CdiUtils.shutdown();
		}
	}
}
