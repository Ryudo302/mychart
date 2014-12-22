package br.com.colbert.mychart.dominio.cancao;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.builder.*;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.validacao.TituloMusical;

/**
 * Uma canção é uma composição musical criada para um ou mais artistas musicais.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
@Entity
@Table(name = "TB_CANCAO")
@SequenceGenerator(name = "codCancaoGenerator", sequenceName = "SEQ_COD_CANCAO")
public class Cancao extends AbstractEntidade<Integer> {

	private static final long serialVersionUID = 7075988463233846463L;

	@Id
	@GeneratedValue(generator = "codCancaoGenerator", strategy = GenerationType.SEQUENCE)
	@Column(name = "COD_CANCAO", unique = true, nullable = false)
	private Integer id;

	@TituloMusical
	@Column(name = "DSC_TITULO", length = 255, unique = false, nullable = false)
	private String titulo;

	@NotEmpty(message = "{br.com.colbert.mychart.constraints.Artistas.message}")
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "TB_ARTISTA_CANCAO", joinColumns = { @JoinColumn(referencedColumnName = "COD_CANCAO") }, inverseJoinColumns = { @JoinColumn(referencedColumnName = "COD_ARTISTA") })
	private List<Artista> artistas;

	public Cancao(String titulo, List<Artista> artistas) {
		this.titulo = titulo;
		this.artistas = artistas != null ? new ArrayList<>(artistas) : Collections.emptyList();
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	Cancao() {
		this(null, Collections.emptyList());
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public List<Artista> getArtistas() {
		return Collections.unmodifiableList(artistas);
	}

	/**
	 * Obtém o artista principal da canção, que é aquele que é o dono da canção (não convidado).
	 * 
	 * @return um opcional contendo o artista principal (pode estar vazio caso a canção ainda não possua artistas definidos)
	 */
	@Transient
	public Optional<Artista> getArtistaPrincipal() {
		// TODO Sempre o primeiro da lista?
		return artistas.size() > 0 ? Optional.of(artistas.get(0)) : Optional.empty();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("titulo", titulo)
				.append("artistas", artistas).toString();
	}
}
