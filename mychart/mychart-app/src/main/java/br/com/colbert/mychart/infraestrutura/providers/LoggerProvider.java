package br.com.colbert.mychart.infraestrutura.providers;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.*;

/**
 * Classe que provê instâncias de {@link Logger}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@ApplicationScoped
public class LoggerProvider implements Serializable {

	private static final long serialVersionUID = -8857479704355681672L;

	/**
	 * Cria um {@link Logger} para a classe solicitada.
	 * 
	 * @param injectionPoint
	 *            ponto de injeção do logger
	 * @return a instância do logger
	 */
	@Produces
	public Logger criarLogger(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger((Class<?>) injectionPoint.getType());
	}
}
