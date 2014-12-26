package br.com.colbert.mychart.dominio.cancao;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.mychart.dominio.artista.Artista;
import br.com.colbert.mychart.infraestrutura.validacao.TituloMusical;

/**
 * Uma canção é uma composição musical criada para um ou mais artistasCancao musicais.
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
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "cancao", orphanRemoval = true)
	@OrderColumn(name = "NUM_ORDEM", nullable = false, insertable = true, updatable = true)
	@Fetch(FetchMode.SELECT)
	private List<ArtistaCancao> artistasCancao;

	/**
	 * Construtor <em>full</em>.
	 * 
	 * @param id
	 *            da canção
	 * @param titulo
	 *            da canção
	 * @param artistas
	 *            da canção
	 */
	Cancao(Integer id, String titulo, Artista... artistas) {
		this.id = id;
		this.titulo = titulo;
		this.artistasCancao = artistas != null ? toArtistasCancao(Arrays.asList(artistas)) : Collections.emptyList();
	}

	/**
	 * Cria uma nova canção com o título e artistas informados.
	 * 
	 * @param titulo
	 *            da canção
	 * @param artistas
	 *            os artistas da canção
	 */
	public Cancao(String titulo, List<Artista> artistas) {
		this(null, titulo, artistas != null ? artistas.toArray(new Artista[artistas.size()]) : (Artista[]) null);
	}

	/**
	 * Cria uma nova canção com o título e artista informado.
	 * 
	 * @param titulo
	 *            da canção
	 * @param artistas
	 *            da canção
	 */
	public Cancao(String titulo, Artista... artistas) {
		this(titulo, artistas != null ? Arrays.asList(artistas) : Collections.emptyList());
	}

	/**
	 * Construtor <code>default</code> sem argumentos utilizado pelo framework ORM.
	 */
	Cancao() {
		this(null, Collections.emptyList());
	}

	private List<ArtistaCancao> toArtistasCancao(List<Artista> artistas) {
		List<ArtistaCancao> artistasCancao = new ArrayList<>(artistas.size());

		for (int i = 0; i < artistas.size(); i++) {
			artistasCancao.add(new ArtistaCancao(artistas.get(i), this, i));
		}

		return artistasCancao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public List<ArtistaCancao> getArtistasCancao() {
		return Collections.unmodifiableList(artistasCancao);
	}

	/**
	 * Obtém todos os {@link Artista}s da canção, respeitando a ordem definida em {@link ArtistaCancao}.
	 * 
	 * @return os artistas (pode ser vazia caso a canção não possua artistas definidos)
	 */
	@Transient
	public List<Artista> getArtistas() {
		return artistasCancao.stream().map(ArtistaCancao::getArtista).collect(Collectors.toList());
	}

	/**
	 * Obtém o artista principal da canção, que é aquele que é o dono da canção (não convidado).
	 * 
	 * @return um opcional contendo o artista principal (pode estar vazio caso a canção ainda não possua artistas definidos)
	 */
	@Transient
	public Optional<Artista> getArtistaPrincipal() {
		List<ArtistaCancao> artistaPrincipalList = artistasCancao.stream()
				.filter(artistaCancao -> artistaCancao.getOrdem().equals(NumberUtils.INTEGER_ZERO)).collect(Collectors.toList());
		return artistaPrincipalList.isEmpty() ? Optional.empty() : Optional.of(artistaPrincipalList.get(0).getArtista());
	}

	/**
	 * Obtém o nome do artista principal da canção.
	 * 
	 * @return o nome do artista principal da canção ou <code>null</code> caso a canção não possua artistas definidos
	 * @see #getArtistaPrincipal()
	 */
	@Transient
	public String getNomeArtistaPrincipal() {
		return getArtistaPrincipal().isPresent() ? getArtistaPrincipal().get().getNome() : null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("titulo", titulo)
				.append("artistas", getArtistas()).toString();
	}
}
