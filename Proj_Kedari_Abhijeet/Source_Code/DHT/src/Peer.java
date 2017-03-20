import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class Peer {

	/**
	 * @param args
	 * @throws IOException
	 */

	public static int SERVERCOUNT=4;
	public static Properties prop = new Properties();
	public static Socket clientSocket[];// = new Socket[3];
	public static BufferedReader consoleReader = new BufferedReader(
			new InputStreamReader(System.in));
	static ObjectOutputStream output[];
	static ObjectInputStream input[];

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String choice = null;
		boolean flag = true;
		@SuppressWarnings("deprecation")
		String block;
		// Put Client as Server
		Server serverObject = new Server();
		serverObject.start();

		// Reading property file
		prop.load(new FileInputStream(new File("config.properties")));
		String ip = (String) prop.get("ip");
		String port = (String) prop.get("port");
		System.out.println("IP: " + ip + "  port: " + port);

		do {
			System.out
					.println("\nMenu:\n1.PUT\n2.GET\n3.DELETE\n4.Exit\nEnter your choice:  ");
			choice = consoleReader.readLine();

			// Connect to all server
			if (flag) {
				connectToServer();
				flag = false;
			}
			switch (choice) {
			case "1":
				block = "0";
				put(block);
				break;
			case "2":
				block = "1";
				get(block);
				break;
			case "3":
				block = "2";
				del(block);
				break;
			case "4":
				System.exit(0);
				break;
			}
		} while (choice != "4");
	}

	private static void del(String block) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		int ascii = 0;
		int val = 0, i;
		String blocks = new String();

		System.out.println("\nEnter the key to delete");
		String key = consoleReader.readLine();
		// t1.setKey(key);
		// t1.setOperation("2");

		val=getHash(key);

		blocks = block + ":" + key;

		delKeyValue(output[val], input[val], blocks);
		
		/*boolean result2 = (boolean) results2;
		if (result2 == true) {
			System.out.println("\nDeleted successufully !!!");
		} else {
			System.out.println("\nNo value associated with Key:" + key
					+ " to delete !!!");
		}*/
		// System.out.println("\nResult:"+result);

	}

	private static void connectToServer() throws IOException, Exception {
		// TODO Auto-generated method stub
		clientSocket = new Socket[SERVERCOUNT];
		output = new ObjectOutputStream[SERVERCOUNT];
		input = new ObjectInputStream[SERVERCOUNT];
	
		for(int i=0;i<SERVERCOUNT;i++)
		{
			String ip = (String) prop.get("ip"+i);
			String serverport = (String) prop.get("port"+i);
			int port = Integer.parseInt(serverport);
			clientSocket[i] = new Socket(ip, port);
			
			output[i] = new ObjectOutputStream(
					clientSocket[i].getOutputStream());
			input[i] = new ObjectInputStream(clientSocket[i].getInputStream());
			
		}
	}

	private static void get(String block) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		int ascii = 0;
		int val = 0, i;
		boolean flag=false;
		String key;
		String blocks = new String();

		do {
			System.out.println("\nEnter the key to search");
			key = consoleReader.readLine();
			if (key.isEmpty()) {
				System.out.println("\nInvalid Input");
				flag = true;
			} else {
				flag = false;
			}
			} while (flag);
		
		
		val=getHash(key);

		blocks = block + ":" + key;

		getKeyValue(output[val], input[val], blocks);
		
		/*String result = (String) results;

		System.out.println("\nKey :" + key + "\tValue: " + result);*/
	}

	private static void put(String block) throws IOException {
		// TODO Auto-generated method stub
		int ascii = 0;
		int val = 0, i, count;
		String key, value;
		String blocks = new String();
		boolean flag = true;
		try {
			do {
				System.out.println("\n Enter the Key to insert:  ");
				key = consoleReader.readLine();
				byte[] b = key.getBytes("UTF-8");
				if (b.length > 24) {
					System.out
							.println("Key lenght exceeds MAXIMUM limit (24 bytes) !!!");
				} else {
					if (key.isEmpty()) {
						System.out.println("\nInvalid Input");
						flag = true;
					} 
					else {
					flag = false;
					}
				}
			} while (flag);

			do {
				System.out.println("\n Enter the Value associated with key  "
						+ key + "  to insert:  ");
				value = consoleReader.readLine();
				byte[] b = value.getBytes("UTF-8");
				if (b.length > 1000) {
					System.out
							.println("Value lenght exceeds MAXIMUM limit (1000 bytes) !!!");
				} else {
					if (value.isEmpty()) {
						System.out.println("\nInvalid Input");
						flag = true;
					} 
					else {
					flag = false;
					}
				}
			} while (flag);

			blocks = block + ":" + key + "#" + value;
			
			val=getHash(key);
				
			putKeyValue(output[val], input[val], blocks);
				
				/*boolean result2 = true;
			if (result2 == true) {
				System.out.println("\nKey " + key
						+ " Inserted successufully !!!");
			} else {
				System.out.println("\nInsert failed !!!");
			}*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getHash (String key)
	{
		int i,val,ascii = 0;
		
		for (i = 0; i < key.length(); i++) {
			val = key.charAt(i);
			ascii = ascii + val;
		}
		val = ascii % SERVERCOUNT;
		
		return val;
		
	}
	
	static void putKeyValue(ObjectOutputStream output, ObjectInputStream input, String blocks) throws IOException, ClassNotFoundException 
	{
		output.writeObject(blocks);
		output.flush();		
		Object results = input.readObject();
	}

	static void getKeyValue(ObjectOutputStream output, ObjectInputStream input, String blocks) throws IOException, ClassNotFoundException 
	{
		output.writeObject(blocks);
		output.flush();
		Object results = input.readObject();
		
		/*String result = (String) results;
		System.out.println("\nResult " + result);*/
	}
	
	static void delKeyValue(ObjectOutputStream output, ObjectInputStream input, String blocks) throws IOException, ClassNotFoundException 
	{
		output.writeObject(blocks);
		output.flush();
		Object results = input.readObject();

	}

}
