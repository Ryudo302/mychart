package br.com.colbert.mychart.dominio.artista.repository;

import br.com.colbert.base.dominio.CrudRepository;
import br.com.colbert.mychart.dominio.artista.Artista;

/**
 * Repositório local de {@link Artista}s. Ele difere de um repositório remoto por permitir operações de inserção, remoção e/ou atualização
 * de elementos.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 * @see ArtistaRepositoryRemoto
 */
public interface ArtistaRepositoryLocal extends CrudRepository<Artista, Integer> {

}
