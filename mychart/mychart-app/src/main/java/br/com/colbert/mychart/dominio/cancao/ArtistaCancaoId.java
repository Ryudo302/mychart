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
	private String idArtista;
	@NotNull
	private String idCancao;

	public ArtistaCancaoId(String idArtista, String idCancao) {
		this.idArtista = idArtista;
		this.idCancao = idCancao;
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	ArtistaCancaoId() {
		this(null, null);
	}

	public String getIdArtista() {
		return idArtista;
	}

	public String getIdCancao() {
		return idCancao;
	}
}
