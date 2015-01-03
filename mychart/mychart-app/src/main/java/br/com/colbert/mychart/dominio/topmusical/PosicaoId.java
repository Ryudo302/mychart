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
public class PosicaoId extends AbstractCompositeId {

	private static final long serialVersionUID = -6392568981419837077L;

	@NotNull
	private Integer numeroTopMusical;
	@NotNull
	private String idCancao;

	public PosicaoId(Integer numeroTopMusical, String idCancao) {
		this.numeroTopMusical = numeroTopMusical;
		this.idCancao = idCancao;
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	PosicaoId() {
		this(null, null);
	}

	public Integer getNumeroTopMusical() {
		return numeroTopMusical;
	}

	public String getIdCancao() {
		return idCancao;
	}
}
