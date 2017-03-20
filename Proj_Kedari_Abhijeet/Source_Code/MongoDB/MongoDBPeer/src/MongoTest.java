
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * @author Abhijeet Kedari
 *
 */
public class MongoTest {

	public static int SERVERCOUNT;
	public static Properties prop = new Properties();
	List<String> ips;
	//int numOfServers;
	int numberOFRequests;
	long startTime = 0, endtime = 0;
	String key, value,str,serverCount,requestsCount;
	
	Logger logObject;
	FileHandler fHandle;
	SimpleFormatter sFormatObject;
	
	//StringBuffer sbf = new StringBuffer();
	//BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("MangoResult.txt")));
	
	MongoDBClient mongoDBClient;	
	
	public MongoTest() throws FileNotFoundException, IOException
	{
		prop = new Properties();
		
		logObject = Logger.getLogger("result");
		fHandle = new FileHandler("result.log");
		logObject.addHandler(fHandle);
		sFormatObject = new SimpleFormatter();
		fHandle.setFormatter(sFormatObject);
		
		ips = new ArrayList<String>();
				
		prop.load(new FileInputStream(new File("config.properties")));

		requestsCount = (String) prop.get("numberOFRequests");
		numberOFRequests = Integer.parseInt(requestsCount);

		serverCount = (String) prop.get("numberOfServers");
		SERVERCOUNT = Integer.parseInt(serverCount);

		for(int i = 0; i < SERVERCOUNT; i++ ){
			ips.add(prop.getProperty("ip" + i));
		}
		mongoDBClient = new MongoDBClient(ips, SERVERCOUNT);
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{

		MongoTest mt= new MongoTest();
		mt.put();
		mt.get();
		mt.remove();
	}

	public void put(){
		try {
			startTime = (int) System.currentTimeMillis();
			for (int i = 0; i < numberOFRequests; i++) {
				key = "a" + i;
				value = "Value" + i;
				mongoDBClient.put(key, value);
			}
			endtime = (int) System.currentTimeMillis();
			logObject.info("PUT Time "+(endtime - startTime));
		} catch (Exception ex) {

		}
	}

	public void get(){
		try {
			startTime = (int) System.currentTimeMillis();
			for (int i = 0; i < numberOFRequests; i++) {
				key = "a" + i;
				str = mongoDBClient.get(key);
			}
			endtime = (int) System.currentTimeMillis();
			logObject.info("GET Time "+(endtime - startTime));
		} catch (Exception ex) {

		}
	}
	
	public void remove(){
		try {
			startTime = (int) System.currentTimeMillis();
			for (int i = 0; i < numberOFRequests; i++) {
				key = "a" + i;
				mongoDBClient.remove(key);
			}
			endtime = (int) System.currentTimeMillis();
			logObject.info("REMOVE Time "+(endtime - startTime));
		} catch (Exception ex) {

		}
	}

}
