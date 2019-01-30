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
		
		
		new Crawler();
		
	}
	

}
