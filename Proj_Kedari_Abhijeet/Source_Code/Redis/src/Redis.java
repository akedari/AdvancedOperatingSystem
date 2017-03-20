import java.util.List;
import redis.clients.jedis.Jedis;


public class Redis {

	public int SERVERCOUNT;
	private static Jedis jedis[];
	
	public Redis(List<String> serverIPs, int numOfServers) {
		int i;
		SERVERCOUNT=numOfServers;
		jedis = new Jedis[numOfServers];
		for (i = 0; i < numOfServers; i++) {
		String str=serverIPs.get(i);
		jedis[i]= new Jedis(str);	
		}
	}
	
	public void put (String key, String value){
		int host = getHash(key);
		jedis[host].set(key,value);
	}

	public String get (String key){
		int host = getHash(key);
		return (jedis[host].get(key));
	}
	
	public void remove (String key){
		int host = getHash(key);
		jedis[host].del(key);
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
