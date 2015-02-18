package br.com.colbert.mychart.dominio.importing;

/**
 * Permite a importação de dados de fontes externas para dentro da aplicação.
 * 
 * @author Thiago Colbert
 * @since 17/02/2015
 *
 * @param <T>
 *            o tipo de fonte dos dados
 */
public interface InformacoesParadaImporter<T> {

	/**
	 * Importa os dados a partir de uma determinada fonte.
	 * 
	 * @param fonte
	 *            dos dados a serem importados
	 * @return a parada musical completa gerada
	 * @throws NullPointerException
	 *             caso a fonte informada seja <code>null</code>
	 */
	ParadaMusical importar(T fonte);
}
