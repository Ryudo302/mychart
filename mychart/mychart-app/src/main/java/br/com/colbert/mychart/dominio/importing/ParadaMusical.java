package br.com.colbert.mychart.dominio.importing;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.builder.*;

import br.com.colbert.mychart.dominio.topmusical.TopMusical;

/**
 * Representa uma parada musical, contendo uma lista de {@link TopMusical}.
 * 
 * @author Thiago Colbert
 * @since 17/02/2015
 */
public class ParadaMusical implements Serializable {

	private static final long serialVersionUID = 3653369733623036234L;

	private List<TopMusical> tops;

	public ParadaMusical(List<TopMusical> tops) {
		this.tops = new ArrayList<>(tops);
	}

	public List<TopMusical> getTops() {
		return Collections.unmodifiableList(tops);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("tops.size", tops.size()).toString();
	}
}
