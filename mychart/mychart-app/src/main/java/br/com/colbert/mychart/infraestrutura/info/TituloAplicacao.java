package br.com.colbert.mychart.infraestrutura.info;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando o título da aplicação.
 * 
 * @author Thiago Colbert
 * @since 18/12/2014
 */
@Qualifier
@Target({ METHOD, FIELD, PARAMETER, TYPE })
@Retention(RUNTIME)
@Documented
public @interface TituloAplicacao {

	/**
	 * O formato do título da aplicação.
	 * 
	 * @return o formato
	 */
	Formato value() default Formato.APENAS_NOME;

	/**
	 * Os diversos formatos que o título da aplicação pode ter.
	 * 
	 * @author Thiago Colbert
	 * @since 18/12/2014
	 */
	public enum Formato {

		/**
		 * Apenas o nome da aplicação.
		 */
		APENAS_NOME,

		/**
		 * Versão completa, com o título e números de versão e build.
		 */
		COMPLETO;
	}
}
