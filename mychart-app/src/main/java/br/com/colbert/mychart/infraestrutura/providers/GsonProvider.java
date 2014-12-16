package br.com.colbert.mychart.infraestrutura.providers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.google.gson.*;

/**
 * Provê instâncias de {@link Gson}.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@ApplicationScoped
public final class GsonProvider {

	@Produces
	public Gson criarGson() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}
}
