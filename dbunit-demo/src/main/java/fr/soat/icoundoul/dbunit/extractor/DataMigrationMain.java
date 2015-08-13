package fr.soat.icoundoul.dbunit.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.TreeSet;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;

public class DataMigrationMain {

	private static IDatabaseConnection getConnexion(String base, String schema) throws Exception {

		// connection à la base source (baseX)
		Class.forName("com.mysql.jdbc.Driver");

		java.sql.Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + base, "root", "");

		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection, schema);
		// On précise qu'on utilise Mysql
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());

		return connection;

	}

	public static void main(String[] args) throws Exception {

		// 1: Extartion d'un client avec tous ses détails
		int numClient = 200;
		IDatabaseConnection connectionBaseX = getConnexion("baseX", "baseX");
		IDataSet ds = TablesDependencyHelper.getAllDataset(connectionBaseX, "t_client", new TreeSet(Arrays.asList(numClient)));
		String fileName = "data_client_" + numClient + ".xml";
		FlatXmlDataSet.write(ds, new FileOutputStream(fileName));

		System.out.println("Export avec succès du client " + numClient + ". Fichier dataSet:" + fileName);

		// 2. Injection du client avec tous ses détails dans la base Z
		IDatabaseConnection connectionBaseZ = getConnexion("baseZ", "baseZ");
		File dataSetFile = new File(fileName);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(dataSetFile);
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
		DatabaseOperation.CLEAN_INSERT.execute(connectionBaseZ, replacementDataSet);

		System.out.println("Client " + numClient + " injecté avec succés dans la base Z");

	}
}
