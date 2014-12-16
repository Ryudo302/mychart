package br.com.colbert.mychart.dominio.topmusical;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import br.com.colbert.base.dominio.AbstractCompositeId;

/**
 * Classe representando o identificador único de {@link Posicao}, correspondendo a um valor composto.
 * 
 * @author Thiago Colbert
 * @since 09/12/2014
 */
@Embeddable
public final class PosicaoId extends AbstractCompositeId {

	private static final long serialVersionUID = -6392568981419837077L;

	@NotNull
	private final Integer numeroTopMusical;
	@NotNull
	private final Integer idCancao;

	public PosicaoId(Integer numeroTopMusical, Integer idCancao) {
		this.numeroTopMusical = numeroTopMusical;
		this.idCancao = idCancao;
	}

	public Integer getNumeroTopMusical() {
		return numeroTopMusical;
	}

	public Integer getIdCancao() {
		return idCancao;
	}
}
