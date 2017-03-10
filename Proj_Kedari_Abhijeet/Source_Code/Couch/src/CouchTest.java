import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class CouchTest {

	
	public static int SERVERCOUNT;
	public static Properties prop = new Properties();
	List<String> ips;
	int numberOFRequests;
	long startTime = 0, endtime = 0;
	String key, value,str,serverCount,requestsCount;
	
	Logger logObject;
	FileHandler fHandle;
	SimpleFormatter sFormatObject;
	
	Couch couchCLient;
	
	public CouchTest()
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
			couchCLient = new Couch(ips, SERVERCOUNT);
		}

		catch (Exception e) {
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CouchTest mt= new CouchTest();
		mt.put();
		mt.get();
		mt.remove();
	}


	private void put() {
		try {
			System.out.println("put");
			startTime = (int) System.currentTimeMillis();
			for (int i = 0; i < numberOFRequests; i++) {
				key = "a" + i;
				value = "Value" + i;
				couchCLient.put(key, value);
			}
			endtime = (int) System.currentTimeMillis();
			logObject.info("PUT Time "+(endtime - startTime));
		} catch (Exception ex) {
		}
		System.out.println("put End");
	}


	private void get() {
		try {
			startTime = (int) System.currentTimeMillis();
			for (int i = 0; i < numberOFRequests; i++) {
				key = "a" + i;
				couchCLient.get(key);
			}
			endtime = (int) System.currentTimeMillis();
			logObject.info("GET Time "+(endtime - startTime));
		} catch (Exception ex) {
		}
	}


	private void remove() {
		try {
			startTime = (int) System.currentTimeMillis();
			for (int i = 0; i < numberOFRequests; i++) {
				key = "a" + i;
				couchCLient.remove(key);
			}
			endtime = (int) System.currentTimeMillis();
			logObject.info("REMOVE Time "+(endtime - startTime));
		} catch (Exception ex) {

		}
	}

}
