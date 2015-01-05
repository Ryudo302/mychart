package br.com.colbert.tests.support;

import java.io.*;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatDtdWriter;

/**
 * Classe que permite a exportação dos dados e metadados do banco de dados para a geração de arquivos DTD e XML do DBUnit.
 * 
 * @author Thiago Colbert
 * @since 16/12/2014
 */
public class DatabaseExport {

	private static final Properties DATASOURCE_PROPERTIES;

	private static final String ARQUIVO_DTD = "src/test/resources/datasets/mychart.dtd";

	static {
		DATASOURCE_PROPERTIES = new Properties();
		try {
			FileInputStream stream = new FileInputStream("src/main/resources/datasources.properties");
			DATASOURCE_PROPERTIES.load(stream);
			IOUtils.closeQuietly(stream);
		} catch (IOException exception) {
			throw new ExceptionInInitializerError(exception);
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DatabaseConnection databaseConnection = null;
		Writer out = null;

		try {
			String dbUrl = DATASOURCE_PROPERTIES.getProperty("resource.mychartds.driverProperties.url");
			System.out.println("Exportando a partir de: " + dbUrl);

			databaseConnection = new DatabaseConnection(DriverManager.getConnection(dbUrl,
					DATASOURCE_PROPERTIES.getProperty("resource.mychartds.driverProperties.user"),
					DATASOURCE_PROPERTIES.getProperty("resource.mychartds.driverProperties.password")));
			IDataSet dataSet = databaseConnection.createDataSet();
			out = new OutputStreamWriter(new FileOutputStream(ARQUIVO_DTD));
			FlatDtdWriter datasetWriter = new FlatDtdWriter(out);
			datasetWriter.setContentModel(FlatDtdWriter.CHOICE);
			datasetWriter.write(dataSet);
			out.flush();
		} finally {
			if (databaseConnection != null) {
				databaseConnection.close();
			}
			if (out != null) {
				out.close();
			}
		}

		try (InputStream inputStream = new FileInputStream(ARQUIVO_DTD)) {
			System.out.println("DTD gerado!");
			System.out.println(IOUtils.toString(inputStream));
		}
	}
}
