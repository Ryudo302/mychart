package br.com.colbert.tests.support;

import java.io.PrintStream;
import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * {@link TestRule} que imprime o nome de cada caso de teste em um stream de saída.
 * 
 * @author Thiago Colbert
 * @since 08/12/2014
 */
public final class PrintTestNameWatcher implements TestRule {

	private static final String FORMATO_MENSAGEM = ">>>>>>>>>> Executando: {0}.{1} <<<<<<<<<<";

	private final PrintStream stream;

	public PrintTestNameWatcher(PrintStream stream) {
		Validate.notNull(stream, "Stream nulo");
		this.stream = stream;
	}

	public PrintTestNameWatcher() {
		this(System.out);
	}

	@Override
	public Statement apply(Statement statement, Description description) {
		stream.println();
		stream.println(MessageFormat.format(FORMATO_MENSAGEM, description.getClassName(), description.getMethodName()));
		stream.println();
		return statement;
	}
}
