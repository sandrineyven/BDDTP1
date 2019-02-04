import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {


	@SuppressWarnings("resource")
	public static void main(String[] args) {

		System.out.println("Connexion a MongoDB...");

		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("TP1");
		MongoCollection<Document> collection = database.getCollection("spells");
		
		//Si les spells ne sont pas dans la db
		if(collection.countDocuments() == 0){
			System.out.println("Recuperation des spells...");
			//Récupération des spells
			Crawler crawler = new Crawler();
			List<Spell> listSpell = crawler.recupSpell();

			//Insertion dans mongoDB
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
			System.out.println("Recuperation des spells: Done");
		}
		
		//MAPREDUCE - MongoDB
		String map ="function() {"
				+ "if(this.Level <= 4)"
				+ "if(this.Classe == 'wizard')"
				+ "if(this.Components[0] == 'V')"
				+ "if(this.Components[1] == null)"
				+ "emit(this.Name,1);}";
		String reduce ="function(key,value) {return;}"; 
		MapReduceIterable<Document> mapReduceResult = collection.mapReduce(map, reduce);
		System.out.println("Resultats mapReduce:");
		
		Iterator<Document> iterator = mapReduceResult.iterator();
		while(iterator.hasNext())
		{
			Document documentResult = (Document)iterator.next();
			System.out.println(documentResult.toString());
		}

	}


}
