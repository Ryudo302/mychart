package br.com.colbert.tests.support;

import java.io.FileOutputStream;

import javax.persistence.EntityManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.hibernate.Session;

import br.com.colbert.mychart.infraestrutura.CdiUtils;

/**
 * Classe que permite a exportação dos dados e metadados do banco de dados para a geração de arquivos DTD e XML do DBUnit.
 * 
 * @author Thiago Colbert
 * @since 16/12/2014
 */
public class DatabaseExport {

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		CdiUtils.init();
		DatabaseConnection databaseConnection = null;

		try {
			EntityManager entityManager = CdiUtils.getBean(EntityManager.class);
			databaseConnection = entityManager.unwrap(Session.class).doReturningWork(connection -> {
				try {
					return new DatabaseConnection(connection);
				} catch (Exception exception) {
					throw new RuntimeException(exception);
				}
			});

			FlatDtdDataSet.write(databaseConnection.createDataSet(), new FileOutputStream(
					"src/test/resources/datasets/mychart.dtd"));
		} finally {
			if (databaseConnection != null) {
				databaseConnection.close();
			}

			CdiUtils.shutdown();
		}
	}
}
