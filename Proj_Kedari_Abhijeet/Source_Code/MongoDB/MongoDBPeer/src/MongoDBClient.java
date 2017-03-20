import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.List;

public class MongoDBClient {

	public int SERVERCOUNT;
	public static Properties prop = new Properties();
	String host=null; 
	MongoClient mongoClient;
	DB db;
	DBCollection collecion;
	BasicDBObject dbObJ;
	
	ArrayList<DBCollection> mongoDBcollectionList;
	//ArrayList<BasicDBObject> basicDBObjList;
	DBCollection dBCollection;
	
	public MongoDBClient(List<String> serverIPs, int numOfServers){
		this.SERVERCOUNT = numOfServers;
		mongoDBcollectionList = new ArrayList<DBCollection>();
		//basicDBObjList = new ArrayList<BasicDBObject>();
		
		for(int i=0; i< numOfServers; i++){
			host = serverIPs+":27017";			
			mongoClient = new MongoClient(host);
			db = mongoClient.getDB("TestDB");
			collecion = db.getCollection("table1");
			mongoDBcollectionList.add(collecion);
			
			dbObJ= new BasicDBObject("Key", "Value");
			mongoDBcollectionList.get(i).insert(dbObJ);
		}
		
	}
	
	public void put (String key, String value){
		int host = getHash(key);
		DBCursor cursor = mongoDBcollectionList.get(host).find();
		BasicDBObject dbObjFetch = (BasicDBObject) cursor.next();
		dbObjFetch.put(key, value);
		mongoDBcollectionList.get(host).save(dbObjFetch);
	}

	public String get (String key){
		int host = getHash(key);
		DBCursor cursor = mongoDBcollectionList.get(host).find();
		BasicDBObject dbObjFetch = (BasicDBObject) cursor.next();
		return (String)dbObjFetch.get(key);
	}
	
	public void remove (String key){
		int host = getHash(key);
		DBCursor cursor = mongoDBcollectionList.get(host).find();
		BasicDBObject dbObjFetch = (BasicDBObject) cursor.next();
		dbObjFetch.remove(key);
		mongoDBcollectionList.get(host).save(dbObjFetch);
	}

	private int getHash(String key) {
		int i,val,ascii = 0;
		
		for (i = 0; i < key.length(); i++) {
			val = key.charAt(i);
			ascii = ascii + val;
		}
		val = ascii % SERVERCOUNT;
		return val;
	}
	
}
