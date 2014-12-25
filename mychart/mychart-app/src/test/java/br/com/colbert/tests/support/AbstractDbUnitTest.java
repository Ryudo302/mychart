package br.com.colbert.tests.support;

import java.io.*;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
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

	@Inject
	protected EntityManager entityManager;

	@Before
	public void setUpDB() throws DatabaseUnitException, SQLException, IOException, ClassNotFoundException {
		entityManager.unwrap(Session.class).doWork(connection -> {
			IDatabaseConnection dbUnitConnection = null;
			try {
				dbUnitConnection = new DatabaseConnection(connection);
				DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, getDataSet());
			} catch (Exception exception) {
				throw new RuntimeException("Erro ao carregar dados no DB", exception);
			} finally {
				if (dbUnitConnection != null) {
					dbUnitConnection.close();
				}
			}
		});
	}

	@After
	public void tearDownDb() throws DatabaseUnitException, SQLException, IOException, ClassNotFoundException {
		entityManager.unwrap(Session.class).doWork(connection -> {
			IDatabaseConnection dbUnitConnection = null;
			try {
				dbUnitConnection = new DatabaseConnection(connection);
				DatabaseOperation.DELETE_ALL.execute(dbUnitConnection, getDataSet());
			} catch (Exception exception) {
				throw new RuntimeException("Erro ao limpar DB", exception);
			} finally {
				if (dbUnitConnection != null) {
					dbUnitConnection.close();
				}
			}
		});
	}

	/**
	 * Obtém o conjunto dos dados do DBUnit utilizados nos testes.
	 * 
	 * @return o conjunto de dados
	 * @throws IOException
	 * @throws DataSetException
	 */
	protected IDataSet getDataSet() throws IOException, DataSetException {
		return new FlatXmlDataSetBuilder().setMetaDataSetFromDtd(getDtdFileInputStream()).build(getDataSetFileInputStream());
	}

	private InputStream getDtdFileInputStream() {
		return getResourceAsStream(DATASET_FILES_FOLDER + File.separatorChar + DTD_FILE_NAME);
	}

	private InputStream getDataSetFileInputStream() {
		return getResourceAsStream(DATASET_FILES_FOLDER + File.separatorChar + getDataSetFileName());
	}

	private InputStream getResourceAsStream(String name) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
	}

	/**
	 * Obtém o nome do arquivo de dados do DBUnit utilizado nos testes.
	 * 
	 * @return o nome do arquivo
	 */
	protected abstract String getDataSetFileName();
}
