import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
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
	public static int SERVERS=8;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String choice = null;
		boolean flag = true;
		@SuppressWarnings("deprecation")
		String block;
		// Put Client as Server
		Server serverObject = new Server();
		serverObject.start();
		
		PeerOneThreads clientasaserver = new PeerOneThreads();
		clientasaserver.start();
		
		// Reading property file
		prop.load(new FileInputStream(new File("config.properties")));
		String ip = (String) prop.get("ip");
		//String port = (String) prop.get("port");
		String port = (String) prop.get("peer");
		//System.out.println("Port: " + port);

		do {
			System.out
					.println("\nMenu:\n1.Register\n2.Search\n3.Exit\nEnter your choice:  ");
			choice = consoleReader.readLine();

			// Connect to all server
			if (flag) {
				connectToServer();
				flag = false;
			}
			switch (choice) {
			case "1":
				block = "0";
				register(block,port);
				break;
			case "2":
				block = "1";
				get(block);
				break;
			case "3":				
				System.exit(0);
				break;
			}
		} while (choice != "3");
	}

	private static void connectToServer() throws IOException, Exception {
		// TODO Auto-generated method stub
		clientSocket = new Socket[8];
		output = new ObjectOutputStream[SERVERS];
		input = new ObjectInputStream[SERVERS];
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

		for (int i = 0; i < SERVERS; i++) {
			output[i] = new ObjectOutputStream(
					clientSocket[i].getOutputStream());
			input[i] = new ObjectInputStream(clientSocket[i].getInputStream());
		}
	}

	private static void get(String block) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		int ascii = 0;
		int val = 0, i,k;
		boolean flag=false;
		String key;
		String blocks = new String();

		do {
			System.out.println("\nEnter the file name to search");
			key = consoleReader.readLine();
			if (key.isEmpty()) {
				System.out.println("\nInvalid Input");
				flag = true;
			} else {
				flag = false;
			}
			} while (flag);
		
		for (i = 0; i < key.length(); i++) {
			val = key.charAt(i);
			ascii = ascii + val;
		}
		val = ascii % SERVERS;

		blocks = block + ":" + key;

		/*if(!getoperation(val, blocks, key))
		{			
			ascii=ascii+1;
			val = ascii % SERVERS;
			getoperation(val, blocks, key);
		}*/
		
		
		if(!getoperation(val, blocks, key))
		{		
			String replicationFactorstr = (String) prop.get("replicationFactor");
			int replicationFactor = Integer.parseInt(replicationFactorstr);
			
			for(k=0;k<replicationFactor;k++)
			{
				ascii=ascii+(k+1);
				val = ascii % SERVERS;
				if(getoperation(val, blocks, key))
				{
					break;
				}
			}
		}
		
	}
	
	public static boolean getoperation(int val, String blocks, String key)
			throws IOException, ClassNotFoundException {

		try {
			output[val].writeObject(blocks);
			output[val].flush();
			int i;
			int servernumber = 0;
			String searchResult = null;
			String[] ipAndPort = null;

			Object results = input[val].readObject();
			String result = (String) results;
			String[] tempString = null;
			ArrayList<String> servers = new ArrayList<String>();

			if (result.equalsIgnoreCase("No value associated with key")) {
				System.out.println("No value associated with key");
				return true;
			}

			else {
				System.out.println("\nFile :" + key
						+ " is available on below address:");
				tempString = result.split("#"); // // $
				for (i = 0; i < tempString.length; i++) {
					System.out.println("\n->" + tempString[i]);
				}

				do {
					System.out
							.println("\nPlease enter the number from which you want to download it.");
					System.out.println("\n[Pleae enter the value between 1 to "
							+ tempString.length + "]");

					servernumber = Integer.parseInt(consoleReader.readLine());
					servernumber = servernumber - 1;
				} while (servernumber < 0
						|| servernumber > (tempString.length - 1));

				searchResult = tempString[servernumber];
				ipAndPort = searchResult.split("@", 2);

				String peerIPAddress = ipAndPort[0];
				int peerPortNumber = Integer.parseInt(ipAndPort[1]);

				fileTransfertoPeer(key, peerIPAddress, peerPortNumber);

				return true;
			}
		} catch (Exception e) {
			return false;
		}

	}

	public static void fileTransfertoPeer(String fileName, String IPAddress,
				int portNumber) throws IOException, Exception {
			
		
		Socket clientSideSocket = null;
		prop.load(new FileInputStream(new File("config.properties")));
		String FOLDERURL = (String) prop.get("peerSourcefolderUrl");		
		try 
		{
			int bytesRead;
			clientSideSocket = new Socket(IPAddress, portNumber);
			
            DataOutputStream dos = new DataOutputStream(clientSideSocket.getOutputStream());
            dos.writeUTF(fileName);
			DataInputStream clientData = new DataInputStream(clientSideSocket.getInputStream());
			fileName=clientData.readUTF();
			OutputStream output=new FileOutputStream(FOLDERURL+""+fileName);
			long size= clientData.readLong();
			byte [] buffer = new byte[1024];
			
			while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
                size -= bytesRead;
            }
            output.close();
            System.out.println("File downloaded successfully !!!");	
            //clientData.close();            
            //System.out.println("File "+fileName+" received from peer.");			
		}
		catch(Exception e)
		{
			//System.out.println("\nServer is down !!!");
		}
				
		
	}

	private static void register(String block, String clientport)
			throws IOException {
		// TODO Auto-generated method stub
		int val = 0, i, j, k, tempascii, tempmod;
		String clientAddress;
		// String[] blocks = null;

		String FOLDERURL = (String) prop.get("peerSourcefolderUrl");
		String replicationFactorstr = (String) prop.get("replicationFactor");
		int replicationFactor = Integer.parseInt(replicationFactorstr);

		try {

			// IPAddress resolving
			String clientIPAddress = null;
			Enumeration en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) en.nextElement();
				for (Enumeration en2 = ni.getInetAddresses(); en2
						.hasMoreElements();) {
					InetAddress addr = (InetAddress) en2.nextElement();
					if (!addr.isLoopbackAddress()) {
						if (addr instanceof Inet4Address) {
							clientIPAddress = addr.getHostAddress();
						}
					}
				}
			}
			// System.out.println("\nIPAddress: " + clientIPAddress);

			// collecting all file names
			File folder = new File(FOLDERURL);
			File[] listOfFiles = folder.listFiles();
			String[] filenames = null;
			ArrayList<String> list = new ArrayList<>();
			ArrayList<String> blocks = new ArrayList<>();
			ArrayList<Integer> ascii = new ArrayList<>();
			ArrayList<Integer> values = new ArrayList<>();
			ArrayList<Integer> vals = new ArrayList<>();
			ArrayList<String> relicationIP = new ArrayList<>();
			ArrayList<String> relicationPort = new ArrayList<>();

			// System.out.println("Total Number of files:" +
			// listOfFiles.length);

			System.out.println("Below files got registered");
			for (i = 0; i < listOfFiles.length; i++) {
				System.out.println("\n" + listOfFiles[i].getName());
				list.add(listOfFiles[i].getName());
				ascii.add(0);
			}

			// HASH FUNCTION
			for (j = 0; j < listOfFiles.length; j++) {
				for (i = 0; i < list.get(j).length(); i++) {
					val = list.get(j).charAt(i);
					ascii.set(j, (ascii.get(j) + val));
				}
				int temp = ascii.get(j) % SERVERS;

				values.add(j, temp);
			}

			// calculate client address to generate string
			clientAddress = clientIPAddress + "@" + clientport;

			for (j = 0; j < listOfFiles.length; j++) {
				blocks.add(j, block + ":" + list.get(j) + ":" + clientAddress);
			}

			for (j = 0; j < listOfFiles.length; j++) {
				String replicaServer = null;
				String str = null;
				tempascii = ascii.get(j);
				
				if (replicationFactor > 0) {

					for (k = 0; k < replicationFactor; k++) {
						str = null;
						tempascii = tempascii + 1;
						tempmod = (tempascii % SERVERS);
						vals.add(tempmod);
						relicationIP.add((String) prop.get("ip"
								+ (tempascii % SERVERS)));
						relicationPort.add((String) prop.get("relicationPort"
								+ (tempascii % SERVERS)));

						str = relicationIP.get(k) + "@" + relicationPort.get(k)
								+ "#";
						if (replicaServer != null) {
							replicaServer = replicaServer + str;
						} else {
							replicaServer = str;
						}
					} //end of for loop with replicationFactor
					
					String tempstr = replicaServer.substring(0,
							replicaServer.length() - 1);
					//System.out.println("replicaServer=" + tempstr);
					String relicationIP1 = (String) prop.get("ip" + values.get(j));
					String relicationPort1 = (String) prop.get("relicationPort"
							+ values.get(j));
					
					blocks.set(j, blocks.get(j) + "#" + relicationIP1 + "@"
							+ relicationPort1 + "#" + tempstr);
					
				} // end of  if condition

				else
				{
					String relicationIP1 = (String) prop.get("ip" + values.get(j));
					String relicationPort1 = (String) prop.get("relicationPort"
							+ values.get(j));
					blocks.set(j, blocks.get(j) + "#" + relicationIP1 + "@"
							+ relicationPort1);
				}//end of else part
				
				
				//System.out.println("block: " + blocks.get(j));

				try {
					putoperation(values.get(j), blocks.get(j), list.get(j));
				} catch (Exception e) {
				}

				for (k = 0; k < replicationFactor; k++) {
					try {
						putoperation(vals.get(k), blocks.get(j), list.get(j));
					} catch (Exception e) {
					}
				}
			}
			
			

		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	public static void putoperation(int val, String blocks, String filename) throws IOException, ClassNotFoundException
	{
		output[val].writeObject(blocks);
		output[val].flush();
		
		Object results = input[val].readObject();
		boolean result = (boolean) results;				
		

	boolean result2 = true;
	if (result2 == true) {
		//System.out.println("\nFile " + filename + " Inserted successufully !!!");
	} else {
		System.out.println("\nInsert failed !!!");
	}
	}

}
