import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {
	 

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//MongoClient mongoClient = new MongoClient();
		
//		 Document doc = new Document("name", "MongoDB")
//	                .append("type", "database")
//	                .append("count", 1)
//	                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
//	                .append("info", new Document("x", 203).append("y", 102));
//		
//		
//		MongoDatabase database = mongoClient.getDatabase("Exemple");
//		MongoCollection<Document> collection = database.getCollection("test");
//		collection.insertOne(doc);
// -------------------------------------------------------------------------------
		
		
		Document codeSource = null;
		try {
			codeSource = Jsoup.connect("http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=1600").get();
		} catch (IOException e1) {
			System.out.println("La connection a echouee");
			e1.printStackTrace();
		}
		//name
		Elements divSpell = codeSource.select("div.SpellDiv");
		Elements spell = divSpell.select("div.heading");
		Elements name = spell.select("p");
		
		//level
		Element level = divSpell.select("p.SPDet").first();
		ArrayList<Element> level1 = level.getAllElements();
		
		//System.out.println(name.text());
		//System.out.println(divSpell);
		String[] liste = level.text().split(" ");
		
		for(int i =0; i< liste.length; i++){
			System.out.println(liste[i]);
		}
		//TODO: Récuperer le level, les composants et spell resitance (boolean)
		//EN faire une boucle sur toute les pages de 1 à 1600
		
	}
	

}
