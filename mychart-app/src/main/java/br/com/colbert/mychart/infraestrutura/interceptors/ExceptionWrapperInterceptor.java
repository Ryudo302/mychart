package br.com.colbert.mychart.infraestrutura.interceptors;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.interceptor.*;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

/**
 * {@link Interceptor} responsável por tratar métodos e tipos anotados com {@link ExceptionWrapper}.
 * 
 * @author Thiago Colbert
 * @since 15/12/2014
 */
@ExceptionWrapper
@Interceptor
public class ExceptionWrapperInterceptor implements Serializable {

	private static final long serialVersionUID = 1467563000742946714L;

	@Inject
	private Logger logger;

	/**
	 * Encapsula as exceções lançadas na execução do método.
	 * 
	 * @param context
	 *            contexto de execução do método
	 * @return resultado da execução do método
	 * @throws Exception
	 *             a exceção encapsuladora ou outra caso ocorra algum erro durante a execução do interceptor
	 */
	@AroundInvoke
	public Object wrapException(InvocationContext context) throws Exception {
		try {
			return context.proceed();
		} catch (Exception wrappedException) {
			ExceptionWrapper annotation = getAnnotation(context);
			assert annotation != null;
			if (ArrayUtils.contains(annotation.de(), wrappedException.getClass())) {
				logger.trace("Encapsulando exceção: {}", wrappedException.toString());
				throw wrapException(wrappedException, annotation, buildExceptionMessage(annotation, context));
			} else {
				logger.trace("NÃO encapsulando exceção: {}", wrappedException.toString());
				throw wrappedException;
			}
		}
	}

	private ExceptionWrapper getAnnotation(InvocationContext context) {
		return context.getMethod().getAnnotation(ExceptionWrapper.class);
	}

	private String buildExceptionMessage(ExceptionWrapper annotation, InvocationContext context) {
		return MessageFormat.format(annotation.mensagem(), context.getParameters());
	}

	private Exception wrapException(Exception exception, ExceptionWrapper annotation, String message)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return (Exception) annotation.para().getConstructor(String.class, Throwable.class).newInstance(message, exception);
	}
}
