package br.com.colbert.mychart;

import org.slf4j.*;

import br.com.colbert.mychart.aplicacao.MainController;
import br.com.colbert.mychart.infraestrutura.CdiUtils;

/**
 * Classe principal da aplicação, responsável pelo <em>bootstrap</em>.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * Método main.
	 *
	 * @param args
	 *            parâmetros de execução
	 */
	public static void main(String... args) {
		LOGGER.info("Iniciando aplicação...");
		try {
			Thread appThread = new Thread(() -> {
				CdiUtils.init();
				CdiUtils.getBean(MainController.class).iniciar();
			});
			appThread.start();
			appThread.join();
		} catch (Exception exception) {
			LOGGER.error("Erro ao iniciar aplicação", exception);
		} finally {
			CdiUtils.shutdown();
		}
	}
}
