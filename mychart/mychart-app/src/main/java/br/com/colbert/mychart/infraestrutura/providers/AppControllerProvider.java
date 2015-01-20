package br.com.colbert.mychart.infraestrutura.providers;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.mvp4j.AppController;
import org.mvp4j.adapter.MVPAdapter;
import org.mvp4j.impl.swing.reflect.*;
import org.mvp4j.impl.swing.swing.SwingAdapter;

/**
 * Provê instâncias de {@link AppController}.
 * 
 * @author Thiago Colbert
 * @since 18/01/2015
 */
@ApplicationScoped
public class AppControllerProvider implements Serializable {

	private static final long serialVersionUID = -3068024438495047285L;

	/**
	 * Obtém uma instância de {@link MVPAdapter}.
	 * 
	 * @return a instância
	 */
	@Produces
	@ApplicationScoped
	public MVPAdapter mvpAdapter() {
		return new SwingAdapter();
	}

	/**
	 * Obtém uma instância de {@link AppController}.
	 * 
	 * @param mvpAdapter
	 * @return a instância
	 */
	@Produces
	@ApplicationScoped
	public AppController appController(MVPAdapter mvpAdapter) {
		AppControllerReflect appController = AppControllerReflectFactory.getAppControllerInstance();
		appController.setAdapter(mvpAdapter);
		return appController;
	}
}
