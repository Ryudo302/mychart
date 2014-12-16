package br.com.colbert.base.dominio;

import java.io.Serializable;

/**
 * Um repositório de entidades.
 * 
 * @author Thiago Colbert
 * @since 13/12/2014
 *
 * @param <T>
 *            tipo de entidade
 * @param <ID>
 *            tipo do identificador único da entidade
 */
public interface Repository<T extends Entidade<ID>, ID extends Serializable> {

}
