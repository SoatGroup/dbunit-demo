package fr.soat.icoundoul.dbunit.extractor;

import java.io.FileOutputStream;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlMetadataHandler;

/**
 * 
 * @author icoundoul
 * @category Service d'extrtation de données depuis une base de données
 */
public class DataExtractorMain {

	public static void main(String[] args) throws Exception {

		// connection à la base source (baseX)
		Class.forName("com.mysql.jdbc.Driver");
		String schema = "basex"; // schéma de la base source
		java.sql.Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/basex", "root", "");

		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection, schema);
		// On précise qu'on utilise Mysql
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());

		// Export de données partielles de la base de données
		QueryDataSet partialDataSet = new QueryDataSet(connection);
		partialDataSet.addTable("CLIENT", "SELECT * FROM t_client WHERE ID='200'");
		FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial_data.xml"));

		// Export de toute la base
		IDataSet fullDataSet = connection.createDataSet();
		FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full_data.xml"));

		// Export des tables dépendantes: export de la table T et de toutes les tables
		// ayant une clé primaire qui est une étrangère sur la table T
		String[] depTableNames = TablesDependencyHelper.getAllDependentTables(connection, "t_client");
		IDataSet depDataSet = connection.createDataSet(depTableNames);
		FlatXmlDataSet.write(depDataSet, new FileOutputStream("dependents.xml"));

		System.out.println("Fichiers dataSet générés avec succès");

	}
}