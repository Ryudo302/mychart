package br.com.colbert.mychart.dominio.cancao;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import br.com.colbert.base.dominio.AbstractCompositeId;

/**
 * ID de entidades do tipo {@link ArtistaCancao}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
@Embeddable
public class ArtistaCancaoId extends AbstractCompositeId {

	private static final long serialVersionUID = -6186511476741490766L;

	@NotNull
	private Integer idArtista;
	@NotNull
	private Integer idCancao;

	public ArtistaCancaoId(Integer idArtista, Integer idCancao) {
		this.idArtista = idArtista;
		this.idCancao = idCancao;
	}

	public Integer getIdArtista() {
		return idArtista;
	}

	public Integer getIdCancao() {
		return idCancao;
	}
}
