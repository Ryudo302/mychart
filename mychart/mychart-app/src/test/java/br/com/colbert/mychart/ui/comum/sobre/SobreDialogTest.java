package br.com.colbert.mychart.ui.comum.sobre;

import br.com.colbert.mychart.infraestrutura.CdiUtils;

/**
 * 
 * @author Thiago Colbert
 * @since 18/12/2014
 */
public class SobreDialogTest {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CdiUtils.init();
			SobreDialog sobreDialog = CdiUtils.getBean(SobreDialog.class);
			sobreDialog.setVisible(true);
		} finally {
			CdiUtils.shutdown();
		}
	}
}
