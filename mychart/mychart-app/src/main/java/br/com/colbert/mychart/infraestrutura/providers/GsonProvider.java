package br.com.colbert.mychart.infraestrutura.providers;

import java.io.Serializable;

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
public class GsonProvider implements Serializable {

	private static final long serialVersionUID = -9143668951013658189L;

	@Produces
	public Gson criarGson() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}
}
