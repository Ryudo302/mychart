package br.com.colbert.tests.support;

import java.io.*;
import java.sql.*;
import java.util.ResourceBundle;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.*;

/**
 * Classe-base para todos os testes de integração que utilizem o DBUnit.
 * 
 * @author Thiago Colbert
 * @since 16/12/2014
 */
public abstract class AbstractDbUnitTest extends AbstractTest {

	private static final String DATASET_FILES_FOLDER = "datasets";
	private static final String DTD_FILE_NAME = "mychart.dtd";

	private static final ResourceBundle DATASOURCE_PROPERTIES = ResourceBundle.getBundle("datasources");

	@Before
	public void setUpDB() throws DatabaseUnitException, SQLException, IOException, ClassNotFoundException {
		final IDatabaseConnection connection = getConnection();
		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
		} finally {
			connection.close();
		}
	}

	@After
	public void tearDownDb() throws DatabaseUnitException, SQLException, IOException, ClassNotFoundException {
		final IDatabaseConnection connection = getConnection();
		try {
			DatabaseOperation.DELETE_ALL.execute(connection, getDataSet());
		} finally {
			connection.close();
		}
	}

	/**
	 * Obtém o conjunto dos dados do DBUnit utilizados nos testes.
	 * 
	 * @return o conjunto de dados
	 * @throws IOException
	 * @throws DataSetException
	 */
	protected IDataSet getDataSet() throws IOException, DataSetException {
		return new FlatXmlDataSetBuilder().setMetaDataSetFromDtd(
				Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(DATASET_FILES_FOLDER + File.separatorChar + DTD_FILE_NAME)).build(
				Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(DATASET_FILES_FOLDER + File.separatorChar + getDataSetFileName()));
	}

	/**
	 * Obtém o nome do arquivo de dados do DBUnit utilizado nos testes.
	 * 
	 * @return o nome do arquivo
	 */
	protected abstract String getDataSetFileName();

	/**
	 * Obtém uma conexão do DBUnit.
	 * 
	 * @return a conexão aberta
	 * @throws ClassNotFoundException
	 *             caso a classe do driver do banco não seja encontrada
	 * @throws SQLException
	 *             caso ocorra algum erro ao abrir conexão JDBC
	 * @throws DatabaseUnitException
	 *             caso ocorra algum erro ao abrir conexão com o DBUnit
	 */
	protected IDatabaseConnection getConnection() throws ClassNotFoundException, SQLException, DatabaseUnitException {
		Class.forName(DATASOURCE_PROPERTIES.getString("resource.mychartds.driverProperties.driverClassName"));
		Connection connection = DriverManager.getConnection(
				DATASOURCE_PROPERTIES.getString("resource.mychartds.driverProperties.url"),
				DATASOURCE_PROPERTIES.getString("resource.mychartds.driverProperties.user"),
				DATASOURCE_PROPERTIES.getString("resource.mychartds.driverProperties.password"));
		return new DatabaseConnection(connection);
	}
}
