package br.com.colbert.mychart.dominio.cancao;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.apache.commons.lang3.builder.CompareToBuilder;

import br.com.colbert.base.dominio.*;
import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Associação entre {@link Artista} e {@link Cancao}.
 * 
 * @author Thiago Colbert
 * @since 23/12/2014
 */
@Entity
@Table(name = "TB_ARTISTA_CANCAO")
public class ArtistaCancao extends AbstractEntidade<ArtistaCancaoId> {

	private static final long serialVersionUID = -2080646278760200072L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idArtista", column = @Column(name = "COD_ARTISTA", unique = false, nullable = false, insertable = true, updatable = false)),
			@AttributeOverride(name = "idCancao", column = @Column(name = "COD_CANCAO", unique = false, nullable = false, insertable = true, updatable = false)) })
	private ArtistaCancaoId id;

	@NotNull(message = "{br.com.colbert.mychart.constraints.Artista.message}")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "COD_ARTISTA", unique = false, nullable = false, insertable = false, updatable = false)
	private Artista artista;

	@NotNull(message = "{br.com.colbert.mychart.constraints.Cancao.message}")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "COD_CANCAO", unique = false, nullable = false, insertable = false, updatable = false)
	private Cancao cancao;

	@NotNull(message = "{br.com.colbert.mychart.constraints.NumeroOrdem.message}")
	@Min(value = 0, message = "{br.com.colbert.mychart.constraints.NumeroOrdem.message}")
	@Column(name = "NUM_ORDEM", unique = false, nullable = false, insertable = true, updatable = true)
	private Integer ordem;

	/**
	 * Cria um novo relacionamento entre o artista e a canção informados.
	 * 
	 * @param artista
	 *            o artista
	 * @param cancao
	 *            a canção
	 * @param ordem
	 *            número de ordem do artista dentro da coleção de artistas associados à canção
	 */
	public ArtistaCancao(Artista artista, Cancao cancao, Integer ordem) {
		this.id = null;
		this.artista = artista;
		this.cancao = cancao;
		this.ordem = ordem;
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	ArtistaCancao() {

	}

	@Override
	public ArtistaCancaoId getId() {
		return id;
	}

	@Override
	public int compareTo(Entidade<ArtistaCancaoId> other) {
		return new CompareToBuilder().append(ordem, ((ArtistaCancao) other).ordem).toComparison();
	}

	public Artista getArtista() {
		return artista;
	}

	public Cancao getCancao() {
		return cancao;
	}

	public Integer getOrdem() {
		return ordem;
	}
}
