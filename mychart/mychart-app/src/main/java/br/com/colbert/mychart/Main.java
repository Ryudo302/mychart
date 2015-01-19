package br.com.colbert.mychart;

import org.apache.log4j.PropertyConfigurator;
import org.jboss.weld.environment.se.StartMain;

public class Main {

	public static void main(String[] args) {
		PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j.properties"));

		StartMain.main(args);
	}
}
