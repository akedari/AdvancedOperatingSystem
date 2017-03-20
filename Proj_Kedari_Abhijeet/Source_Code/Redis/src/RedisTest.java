import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class RedisTest {
	
	public static int SERVERCOUNT;
	public static Properties prop = new Properties();
	List<String> ips;
	int numberOFRequests;
	long startTime = 0, endtime = 0;
	String key, value,str,serverCount,requestsCount;
	
	Logger logObject;
	FileHandler fHandle;
	SimpleFormatter sFormatObject;
	
	Redis  redisClient;
	
	public RedisTest()
	{
		prop = new Properties();
		
		logObject = Logger.getLogger("result");
		try {
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
			for (int i = 0; i < SERVERCOUNT; i++) {
				ips.add(prop.getProperty("ip" + i));
			}
			redisClient = new Redis(ips, SERVERCOUNT);
		}

		catch (Exception e) {
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RedisTest mt= new RedisTest();
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
				redisClient.put(key, value);
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
				redisClient.get(key);
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
				redisClient.remove(key);
			}
			endtime = (int) System.currentTimeMillis();
			logObject.info("REMOVE Time "+(endtime - startTime));
		} catch (Exception ex) {

		}
	}
	
}
