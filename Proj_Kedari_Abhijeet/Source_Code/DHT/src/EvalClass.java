import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class EvalClass {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
	
	public static Socket clientSocket[];
	static ObjectOutputStream output[];
	static ObjectInputStream input[];
	public static Properties prop = new Properties();
	public static int numServer=4;
	public static StringBuffer sbf;
	public static BufferedWriter bwr;
	
	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub

		StringBuffer sbf = new StringBuffer();
		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("Result.txt")));
		
		System.out.println("Inside Evalution Class main");
		prop.load(new FileInputStream(new File("config.properties")));
		
		String numberofrequestss = (String) prop.get("numberofrequests");
		int numberofrequests = Integer.parseInt(numberofrequestss);
		
		connectToServer();
		long startTime = 0, endtime = 0;
		startTime = (int) System.currentTimeMillis();
		register(numberofrequests);
		endtime = (int) System.currentTimeMillis();
		System.out.println("Register DIfference: " + (endtime - startTime));
		sbf.append("Register: "+(endtime - startTime));
        sbf.append(System.getProperty("line.separator"));
		
		startTime = 0; endtime = 0;
		startTime = (int) System.currentTimeMillis();
		search(numberofrequests);
		endtime = (int) System.currentTimeMillis();
		System.out.println("Search DIfference: " + (endtime - startTime));
		sbf.append(System.getProperty("line.separator"));
		sbf.append("Search: "+(endtime - startTime));
        sbf.append(System.getProperty("line.separator"));
		
		startTime = 0; endtime = 0;
		startTime = (int) System.currentTimeMillis();
		delete(numberofrequests);
		endtime = (int) System.currentTimeMillis();
		System.out.println("Delete DIfference: " + (endtime - startTime));
		sbf.append(System.getProperty("line.separator"));
		sbf.append("Delete: "+(endtime - startTime));
        sbf.append(System.getProperty("line.separator"));
        
        bwr.write(sbf.toString());
        bwr.flush();
        bwr.close();
        
        
	}
	
	private static void search(int requests) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		int i,numRequests=requests,hashCode;
		
		String operation="1";
		String[] keys=new String[requests];
		String[] blocks= new String[requests];
		
		for(i=0;i<numRequests;i++)
		{
			keys[i]="a"+i;
			blocks[i] = operation + ":" + keys[i];
		}
		
		for(i=0;i<numRequests;i++)
		{
			hashCode=getHashE(keys[i]);
			Peer.getKeyValue(output[hashCode], input[hashCode], blocks[i]);
		}
	}

	private static void delete(int requests) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		int i,numRequests=requests,hashCode;
		
		String operation="2";
		String[] keys=new String[requests];
		String[] blocks= new String[requests];
		
		
		for(i=0;i<numRequests;i++)
		{
			keys[i]="a"+i;
			blocks[i] = operation + ":" + keys[i];
		}
		
		for(i=0;i<numRequests;i++)
		{
			hashCode=getHashE(keys[i]);
			Peer.delKeyValue(output[hashCode], input[hashCode], blocks[i]);
		}
	}

	private static void register(int requests) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		int i,numRequests=requests,hashCode;
		String operation="0";
		String value="Evaluation";
		String[] keys=new String[requests];
		String[] blocks= new String[requests];

		for(i=0;i<numRequests;i++)
		{
			keys[i]="a"+i;
			blocks[i] = operation + ":" + keys[i] + "#" + value;
		}
			
		for(i=0;i<numRequests;i++)
		{
			hashCode=getHashE(keys[i]);
			Peer.putKeyValue(output[hashCode], input[hashCode], blocks[i]);
		}
	}

	public static int getHashE (String key)
	{
		int i,val,ascii = 0;
		
		for (i = 0; i < key.length(); i++) {
			val = key.charAt(i);
			ascii = ascii + val;
		}
		val = ascii % numServer;
		
		return val;
		
	}
	
	private static void connectToServer() throws IOException, Exception {
		// TODO Auto-generated method stub\
		
		System.out.println("Inside ConnectTOServer()");
		int i;
		clientSocket = new Socket[numServer];
		output = new ObjectOutputStream[numServer];
		input = new ObjectInputStream[numServer];
		
		//connecting to all server
		for (i=0;i<numServer;i++)
		{
			String ip = (String) prop.get("ip"+i);
			System.out.println("ip:"+ip);
			String serverport = (String) prop.get("port"+i);
			int port = Integer.parseInt(serverport);
			System.out.println("port"+port);
			clientSocket[i] = new Socket(ip, port);
			System.out.println("5");
		}
		
		//creating input output streams for all server
		for (i = 0; i < numServer; i++) {
			output[i] = new ObjectOutputStream(
					clientSocket[i].getOutputStream());
			input[i] = new ObjectInputStream(clientSocket[i].getInputStream());
		}
	}

}
