import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {

	public static void main(String[] args) {

		//Connexion MongoDB
		System.out.println("Connexion MongoDB...");
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("TP1");
		MongoCollection<Document> collection = database.getCollection("spells");

		//Connexion SQLite
		System.out.println("Connexion SQLite...");
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			//Creation de la table si elle n'existe pas
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS SPELLS " 
					+ "(ID INT PRIMARY KEY     NOT NULL," 
					+ " NAME           TEXT    NOT NULL, "  
					+ " CLASSE         TEXT     NOT NULL, "  
					+ " LEVEL       	  TEXT, "  
					+ " COMPONENT1      TEXT, "
					+ " COMPONENT2      TEXT, "
					+ " COMPONENT3      TEXT, "
					+ " RESISTANCE	  INT)"; 
			stmt.executeUpdate(sql);


		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}

		//SQLITE - Si les spells ne sont pas dans la db
		int presenceSQLite = 0;

		try {
			String sql = "SELECT COUNT(ID) FROM SPELLS";
			ResultSet resultat = stmt.executeQuery(sql); 
			System.out.println(resultat.getInt(1));
			if(resultat.getInt(1) > 0){
				presenceSQLite = 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		//MONGODB - Si les spells ne sont pas dans la db
		if(collection.countDocuments() == 0 || presenceSQLite == 0){
			System.out.println("Recuperation des spells...");
			//Récupération des spells
			Crawler crawler = new Crawler();
			List<Spell> listSpell = crawler.recupSpell();

			//Insertion dans mongoDB
			if(collection.countDocuments() == 0){
				for(int i=0; i<listSpell.size(); i++){
					//Creation du document json pour mongoDB
					Document doc = new Document("ID", i)
							.append("Name", listSpell.get(i).getName())
							.append("Classe", listSpell.get(i).getClasse())
							.append("Level", listSpell.get(i).getLevel())
							.append("Components", listSpell.get(i).getComponents())
							.append("Spell resistance", listSpell.get(i).isResistance());
					//Insertion du document
					collection.insertOne(doc);
				}
			}else if(presenceSQLite == 0){
				//insert
				for(int i=0; i<listSpell.size(); i++){
					String sql = "INSERT INTO SPELLS (ID,NAME,CLASSE,LEVEL,COMPONENT1,RESISTANCE) VALUES ("
							+ i + ", \""
							+ listSpell.get(i).getName()   + "\", \"" 
							+ listSpell.get(i).getClasse() + "\", \""
							+ listSpell.get(i).getLevel()  + "\", \""
							+ listSpell.get(i).getComponents().get(0) + "\", \""
							+ listSpell.get(i).isResistance() + "\");"; 

					System.out.println(sql);
					try {
						stmt.executeQuery(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					if(listSpell.get(i).getComponents().size() > 1 ){
						String newsql = "UPDATE SPELLS SET COMPONENT2 = \""
								+ listSpell.get(i).getComponents().get(1) + "\" "
								+ "WHERE ID = "+ i +";";
						try {
							System.out.println(newsql);
							stmt.executeQuery(newsql);

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(listSpell.get(i).getComponents().size() > 2){
							String newsql1 = "UPDATE SPELLS SET COMPONENT3 = \""
									+ listSpell.get(i).getComponents().get(2) + "\" "
									+ "WHERE ID = "+ i +";";
							try {
								System.out.println(newsql1);
								stmt.executeQuery(newsql1);

							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}


				}
			}
			System.out.println("Recuperation des spells: Done");
		}





		//MAPREDUCE - MongoDB
		String map ="function() {"
				//Tri: Tous les niveaux <= 4
				+ "if(this.Level <= 4)"
				//Classe 'wizard'
				+ "if(this.Classe == 'wizard')"
				//Composante verbale
				+ "if(this.Components[0] == 'V')"
				//Composante verbale uniquement
				+ "if(this.Components[1] == null)"
				//Renvoie le nom du spell
				+ "emit(this.Name,1);}";

		//Le tri se fait seulement avec map, la fonction reduce n'est pas utilisee
		String reduce ="function(key,value) {return;}"; 


		MapReduceIterable<Document> mapReduceResult = collection.mapReduce(map, reduce);
		System.out.println("Resultats mapReduce:");

		//Affichage des resultats
		Iterator<Document> iterator = mapReduceResult.iterator();
		while(iterator.hasNext())
		{
			Document documentResult = (Document)iterator.next();
			System.out.println(documentResult.toString());
		}
		
		//TODO: requete SQLite
		//TODO: afficher les resultats joliement
		//TODO: close les db propremement, clean code
		

	}

}
