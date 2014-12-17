package br.com.colbert.mychart.infraestrutura.interceptors;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;
import java.text.MessageFormat;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

import org.apache.commons.lang3.StringUtils;

/**
 * Qualificador indicando que deve haver o encapsulamento de exceções no método ou tipo anotado.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Documented
public @interface ExceptionWrapper {

	/**
	 * As exceções que serão encapsuladas quando lançadas. Por padrão, todas as exceções serão tratadas.
	 * 
	 * @return as classes das exceções de origem
	 */
	@Nonbinding
	Class<?>[] de() default Exception.class;

	/**
	 * A exceção que será gerada encapsulando as exceções do(s) tipo(s) {@link #de()}.
	 * 
	 * @return a classe da exceção destino
	 */
	@Nonbinding
	Class<?> para() default RuntimeException.class;

	/**
	 * Mensagem a ser utilizada na criação da exceção encapsuladora. São aceitos parâmetros indexados - <code>{n}</code> - dentro
	 * da mensagem. Os valores passados como parâmetros durante a formatação da mensagem serão os mesmos parâmetros do método que
	 * foi invocado e que lançou a exceção.
	 * 
	 * @return a mensagem
	 * @see MessageFormat
	 */
	@Nonbinding
	String mensagem() default StringUtils.EMPTY;
}
