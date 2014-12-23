package br.com.colbert.mychart.dominio.cancao;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.apache.commons.lang3.builder.*;
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
	@OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY, mappedBy = "cancao", orphanRemoval = true)
	@OrderColumn(name = "NUM_ORDEM", nullable = false, insertable = true, updatable = true)
	private List<ArtistaCancao> artistasCancao;

	/**
	 * Cria uma nova canção com o título e artistas informados.
	 * 
	 * @param titulo
	 *            da canção
	 * @param artistas
	 *            os artistas da canção
	 */
	public Cancao(String titulo, List<Artista> artistas) {
		this.titulo = titulo;
		this.artistasCancao = artistas != null ? toArtistasCancao(artistas) : Collections.emptyList();
	}

	private List<ArtistaCancao> toArtistasCancao(List<Artista> artistas) {
		ArrayList<ArtistaCancao> artistasCancao = new ArrayList<>(artistas.size());

		for (int i = 0; i < artistas.size(); i++) {
			artistasCancao.add(new ArtistaCancao(artistas.get(i), this, i));
		}

		return artistasCancao;
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
	 * @return um opcional contendo o artista principal (pode estar vazio caso a canção ainda não possua artistasCancao definidos)
	 */
	@Transient
	public Optional<Artista> getArtistaPrincipal() {
		// TODO Sempre o primeiro da lista?
		return artistasCancao.size() > 0 ? Optional.of(artistasCancao.get(0).getArtista()) : Optional.empty();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("titulo", titulo)
				.append("artistasCancao", artistasCancao).toString();
	}
}
