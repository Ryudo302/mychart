package br.com.colbert.mychart.dominio.artista;

import br.com.colbert.mychart.infraestrutura.util.EnumUtils;

/**
 * Os diversos tipos de artistas existentes.
 * 
 * @author Thiago Colbert
 * @since 07/12/2014
 */
public enum TipoArtista {

	/**
	 * Um artista do sexo masculino que trabalha solo.
	 */
	MASCULINO_SOLO,

	/**
	 * Uma artista do sexo feminino que trabalha solo.
	 */
	FEMININO_SOLO,

	/**
	 * Uma dupla, trio, grupo ou banda, envolvendo um ou mais integrantes.
	 */
	GRUPO_OU_BANDA,

	/**
	 * Artista desconhecido.
	 */
	DESCONHECIDO;

	@Override
	public String toString() {
		return EnumUtils.toFormattedString(this);
	}
}
