package br.com.colbert.mychart.infraestrutura.eventos.topmusical;

import java.io.Serializable;

import org.apache.commons.lang3.builder.*;

import br.com.colbert.mychart.dominio.cancao.Cancao;

/**
 * Evento que indica uma mudança dentro do top musical, como estreia ou alteração de posições de canções.
 * 
 * @author Thiago Colbert
 * @since 09/12/2014
 */
public final class MudancaTopMusicalEvent implements Serializable {

	private static final long serialVersionUID = -7712785922000949756L;

	private final Integer numeroPosicao;
	private final Cancao cancao;

	/**
	 * Cria um novo evento com o número da posição da canção que ocasionou o evento, além da própria canção.
	 * 
	 * @param numeroPosicao
	 * @param cancao
	 */
	public MudancaTopMusicalEvent(Integer numeroPosicao, Cancao cancao) {
		this.numeroPosicao = numeroPosicao;
		this.cancao = cancao;
	}

	public Integer getNumeroPosicao() {
		return numeroPosicao;
	}

	public Cancao getCancao() {
		return cancao;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
