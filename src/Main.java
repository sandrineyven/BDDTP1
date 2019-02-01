import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {


	@SuppressWarnings("resource")
	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("TP1");
		MongoCollection<Document> collection = database.getCollection("spells");
		
		//Si les spells ne sont pas dans la db
		if(collection.countDocuments() == 0){
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
		}

	}


}
