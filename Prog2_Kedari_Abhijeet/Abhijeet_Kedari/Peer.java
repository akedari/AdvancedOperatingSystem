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

		for (i = 0; i < key.length(); i++) {
			val = key.charAt(0);
			ascii = ascii + val;
		}
		val = ascii % 8;

		blocks = block + ":" + key;

		output[val].writeObject(blocks);
		output[val].flush();

		Object results2 = input[val].readObject();
		boolean result2 = (boolean) results2;
		if (result2 == true) {
			System.out.println("\nDeleted successufully !!!");
		} else {
			System.out.println("\nNo value associated with Key:" + key
					+ " to delete !!!");
		}
		// System.out.println("\nResult:"+result);

	}

	private static void connectToServer() throws IOException, Exception {
		// TODO Auto-generated method stub
		clientSocket = new Socket[8];
		output = new ObjectOutputStream[8];
		input = new ObjectInputStream[8];
		// connecting to peer 1
		String ip0 = (String) prop.get("ip0");
		String serverport0 = (String) prop.get("port0");
		int port0 = Integer.parseInt(serverport0);
		clientSocket[0] = new Socket(ip0, port0);

		// connecting to peer 2
		String ip1 = (String) prop.get("ip1");
		String serverport1 = (String) prop.get("port1");
		int port1 = Integer.parseInt(serverport1);
		clientSocket[1] = new Socket(ip1, port1);

		// connecting to peer 3
		String ip2 = (String) prop.get("ip2");
		String serverport2 = (String) prop.get("port2");
		int port2 = Integer.parseInt(serverport2);
		clientSocket[2] = new Socket(ip2, port2);

		// connecting to peer 4
		String ip3 = (String) prop.get("ip3");
		String serverport3 = (String) prop.get("port3");
		int port3 = Integer.parseInt(serverport3);
		clientSocket[3] = new Socket(ip3, port3);

		// connecting to peer 5
		String ip4 = (String) prop.get("ip4");
		String serverport4 = (String) prop.get("port4");
		int port4 = Integer.parseInt(serverport4);
		clientSocket[4] = new Socket(ip4, port4);

		// connecting to peer 6
		String ip5 = (String) prop.get("ip5");
		String serverport5 = (String) prop.get("port5");
		int port5 = Integer.parseInt(serverport5);
		clientSocket[5] = new Socket(ip5, port5);

		// connecting to peer 7
		String ip6 = (String) prop.get("ip6");
		String serverport6 = (String) prop.get("port6");
		int port6 = Integer.parseInt(serverport6);
		clientSocket[6] = new Socket(ip6, port6);

		// connecting to peer 8
		String ip7 = (String) prop.get("ip7");
		String serverport7 = (String) prop.get("port7");
		int port7 = Integer.parseInt(serverport7);
		clientSocket[7] = new Socket(ip7, port7);

		for (int i = 0; i < 8; i++) {
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
		
		for (i = 0; i < key.length(); i++) {
			val = key.charAt(0);
			ascii = ascii + val;
		}
		val = ascii % 8;

		blocks = block + ":" + key;

		output[val].writeObject(blocks);
		output[val].flush();

		Object results = input[val].readObject();
		String result = (String) results;

		System.out.println("\nKey :" + key + "\tValue: " + result);

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
			
				for (i = 0; i < key.length(); i++) {
					val = key.charAt(0);
					ascii = ascii + val;
				}
				val = ascii % 8;

				output[val].writeObject(blocks);
				output[val].flush();
				Object results = input[val].readObject();
				boolean result = (boolean) results;

			boolean result2 = true;
			if (result2 == true) {
				System.out.println("\nKey " + key
						+ " Inserted successufully !!!");
			} else {
				System.out.println("\nInsert failed !!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
