package fr.soat.icoundoul.dbunit.extractor;

import java.io.File;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;

/**
 *  
 * @author icoundoul
 * @category Service d'injection de données pour peupler une base de données
 * 
 */
public class DataInjectorMain {

	public static void main(String[] args) throws Exception {

		// connection à la base source (baseX)
		Class.forName("com.mysql.jdbc.Driver");
		String schema = "basey"; // shémade la base source
		java.sql.Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/basey", "root", "");

		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection, schema);
		// On précise qu'on utilise Mysql
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());

		// On peut injecter n'importe quel fichier XML. Ici on injecte le fichier full_data.xml
		File dataSetFile = new File("full_data.xml");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(dataSetFile);
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
		DatabaseOperation.CLEAN_INSERT.execute(connection, replacementDataSet);

		System.out.println("Fichier dataSet injecté avec succès!");

	}
}
