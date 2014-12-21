package br.com.colbert.mychart.dominio.artista;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.*;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.mychart.dominio.cancao.Cancao;
import br.com.colbert.mychart.infraestrutura.validacao.Nome;

/**
 * Um artista é um indíviduo ou grupo dentro do ramo musical. É alguém que encabeça trabalhos musicais como canções.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
@Entity
@Table(name = "TB_ARTISTA")
@SequenceGenerator(name = "codArtistaGenerator", sequenceName = "SEQ_COD_ARTISTA")
public class Artista extends AbstractEntidade<Integer> {

	public static final Artista ARTISTA_NULL = new Artista(null, TipoArtista.DESCONHECIDO);

	private static final long serialVersionUID = -5953280230091975040L;

	@Id
	@GeneratedValue(generator = "codArtistaGenerator", strategy = GenerationType.SEQUENCE)
	@Column(name = "COD_ARTISTA", unique = true, nullable = false)
	private Integer id;

	@Nome
	@Column(name = "NOM_ARTISTA", length = 255, unique = false, nullable = false)
	private String nome;

	@NotNull(message = "{br.com.colbert.mychart.constraints.TipoArtista.message}")
	@Column(name = "DSC_TIPO", length = 30, unique = false, nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoArtista tipo;

	@ManyToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "artistas")
	private Set<Cancao> cancoes;

	public Artista(String nome, TipoArtista tipo) {
		this.nome = nome;
		this.tipo = tipo;
		this.cancoes = Collections.emptySet();
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	Artista() {
		this(null, null);
	}

	@Override
	public Integer getId() {
		return id;
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

	public Set<Cancao> getCancoes() {
		return Collections.unmodifiableSet(cancoes);
	}

	/**
	 * Verifica se o artista possui canções cadastradas.
	 * 
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean getPossuiCancoes() {
		return cancoes.size() > 0;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("nome", nome)
				.append("tipo", tipo).toString();
	}
}
