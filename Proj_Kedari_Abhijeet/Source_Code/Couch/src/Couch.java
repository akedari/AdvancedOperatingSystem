import java.util.List;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;



public class Couch {


	public int SERVERCOUNT;
	private static Session dbSession[];
	Database db[];
	
	public Couch(List<String> serverIPs, int numOfServers) {
		int i;
		SERVERCOUNT=numOfServers;
		dbSession=new Session[numOfServers];
		db=new Database[numOfServers];
		
		for (i = 0; i < numOfServers; i++) {
		String str=serverIPs.get(i);
		
		dbSession[i] = new Session(str,5984);
		dbSession[i].createDatabase("couchtt");
		db[i]=dbSession[i].getDatabase("couchtt");
		}
	}
	
	public void put (String key, String value){
		int hash = getHash(key);
		Document doc = new Document();
		doc.setId(key);
        doc.put("value", value);
		db[hash].saveDocument(doc);
	}

	public String get(String key){
		int hash = getHash(key);
		Document d = db[hash].getDocument(key);
		String str= (String)d.get("value");
		return str;
	}
	
	public void remove (String key){
		int hash = getHash(key);
		Document d = db[hash].getDocument(key);
		db[hash].deleteDocument(d);
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
