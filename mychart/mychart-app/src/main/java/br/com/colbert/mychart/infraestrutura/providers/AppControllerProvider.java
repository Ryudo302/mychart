package br.com.colbert.mychart.infraestrutura.providers;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.mvp4j.AppController;
import org.mvp4j.impl.reflect.*;

import br.com.colbert.mychart.infraestrutura.mvp.ExtendedSwingAdapter;

/**
 * Provê instâncias de {@link AppController}.
 * 
 * @author Thiago Colbert
 * @since 18/01/2015
 */
public class AppControllerProvider implements Serializable {

	private static final long serialVersionUID = -3068024438495047285L;

	/**
	 * Obtém uma instância de {@link AppController}.
	 * 
	 * @return a instância
	 */
	@Produces
	@ApplicationScoped
	public AppController appController() {
		AppControllerReflect appController = AppControllerReflectFactory.getAppControllerInstance();
		appController.setAdapter(new ExtendedSwingAdapter());
		return appController;
	}
}
