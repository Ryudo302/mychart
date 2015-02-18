package br.com.colbert.mychart.dominio.artista;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.*;

import br.com.colbert.base.dominio.*;
import br.com.colbert.mychart.dominio.cancao.ArtistaCancao;
import br.com.colbert.mychart.infraestrutura.validacao.Nome;

/**
 * Um artista é um indíviduo ou grupo dentro do ramo musical. É alguém que encabeça trabalhos musicais como canções.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
@Entity
@Table(name = "TB_ARTISTA")
public class Artista extends AbstractEntidade<String> implements Cloneable {

	public static final Artista ARTISTA_NULL = new Artista(null, null, TipoArtista.DESCONHECIDO);

	private static final long serialVersionUID = -5953280230091975040L;

	@Id
	@NotNull
	@Column(name = "COD_ARTISTA", unique = true, nullable = false)
	private String id;

	@Nome
	@Column(name = "NOM_ARTISTA", length = 255, unique = false, nullable = false)
	private String nome;

	@NotNull(message = "{br.com.colbert.mychart.constraints.TipoArtista.message}")
	@Column(name = "DSC_TIPO", length = 30, unique = false, nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoArtista tipo;

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "artista", orphanRemoval = true)
	private Set<ArtistaCancao> cancoesArtista;

	@Transient
	private boolean persistente;

	/**
	 * Cria um novo artista com o ID, nome e tipo informados.
	 * 
	 * @param id
	 *            o ID do artista
	 * @param nome
	 *            do artista
	 * @param tipo
	 *            de artista
	 */
	public Artista(String id, String nome, TipoArtista tipo) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.cancoesArtista = Collections.emptySet();
	}

	/**
	 * Cria um novo artista com o nome e tipo informados.
	 * 
	 * @param nome
	 *            do artista
	 * @param tipo
	 *            de artista
	 */
	public Artista(String nome, TipoArtista tipo) {
		this(null, nome, tipo);
	}

	/**
	 * Cria um novo artista com o nome informado e o tipo {@link TipoArtista#DESCONHECIDO}.
	 * 
	 * @param nome
	 *            do artista
	 */
	public Artista(String nome) {
		this(nome, TipoArtista.DESCONHECIDO);
	}

	/**
	 * Cria um novo artista a partir das informações de outro.
	 * 
	 * @param artista
	 *            o outro artista
	 */
	public Artista(Artista artista) {
		this(artista.id, artista.nome, artista.tipo);
		persistente = artista.persistente;
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	Artista() {
		this(null, null);
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoArtista getTipo() {
		return tipo;
	}

	public Set<ArtistaCancao> getCancoesArtista() {
		return Collections.unmodifiableSet(cancoesArtista);
	}

	/**
	 * Verifica se o artista é persistente - ou seja, se está salvo na base de dados.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean getPersistente() {
		return persistente;
	}

	@PostLoad
	@PostPersist
	protected void persistente() {
		persistente = true;
	}

	@PostRemove
	protected void transiente() {
		persistente = false;
	}

	/**
	 * Verifica se o artista possui canções cadastradas.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	@Transient
	public boolean getPossuiCancoes() {
		return CollectionUtils.isNotEmpty(cancoesArtista);
	}

	@Transient
	public List<TipoArtista> getTiposArtistas() {
		return Arrays.asList(TipoArtista.values());
	}

	@Override
	public Artista clone() {
		return new Artista(this);
	}

	@Override
	protected int compareToNullId(Entidade<String> other) {
		return new CompareToBuilder().append(nome, ((Artista) other).nome).toComparison();
	}

	@Override
	protected boolean equalsWithNullId(Entidade<String> other) {
		return new EqualsBuilder().append(getNome(), ((Artista) other).getNome()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("nome", nome)
				.append("tipo", tipo).toString();
	}
}
