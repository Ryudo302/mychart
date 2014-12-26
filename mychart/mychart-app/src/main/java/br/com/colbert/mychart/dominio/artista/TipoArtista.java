package br.com.colbert.mychart.dominio.artista;

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
		String[] splitNames = name().toLowerCase().split("_");
		StringBuffer fixedName = new StringBuffer();

		for (int i = 0; i < splitNames.length; i++) {
			String firstLetter = splitNames[i].substring(0, 1).toUpperCase(), restOfWord = splitNames[i].substring(1), spacer = i == splitNames.length ? ""
					: " ";

			fixedName.append(firstLetter).append(restOfWord).append(spacer);
		}

		return fixedName.toString().trim();
	}
}
