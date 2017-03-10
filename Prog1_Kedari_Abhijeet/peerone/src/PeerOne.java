import java.io.BufferedOutputStream;
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

public class PeerOne {

	public static Properties prop = new Properties();

	public static void main(String[] args) throws IOException, Exception {
		
		prop.load(new FileInputStream(new File("config.properties")));
		String FOLDERURL= (String) prop.get("peerDownlaodfolderUrl");
		String clientport=(String) prop.get("peer");
		int CLIENTPORT= Integer.parseInt(clientport);	
		String choice =null;		
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
			
		PeerOneThreads clientasaserver= new PeerOneThreads();
		clientasaserver.start();
		
		do {
			System.out
					.println("\nMenu:\n1.Register\n2.Search\n3.Exit\nEnter your choice:  ");
			choice = consoleReader.readLine();

			switch (choice) {
			case "1":
				registerPeer();
				break;
			case "2":
				search();
				break;
			case "3":
				System.exit(0);
				break;
			}
		} while (choice != "3");
	}

	private static void search() throws Exception {
		// TODO Auto-generated method stub
		String serverport = (String) prop.get("indexServerPort");
		int SERVERPORT = Integer.parseInt(serverport);
		String serverIP=(String)prop.get("serverIPAddress");
		Socket clientSocket = new Socket(serverIP, SERVERPORT);
		
		ObjectOutputStream output = new ObjectOutputStream(
				clientSocket.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(
				clientSocket.getInputStream());
		BufferedReader consoleReader = new BufferedReader(
				new InputStreamReader(System.in));

		System.out.println("\nEnter the File name you want to search");
		String fileName = consoleReader.readLine();

		output.writeObject("Search");
		output.flush();

		Object ack = input.readObject();
		String ackstr = (String) ack;
		System.out.println(ackstr);

		output.writeObject(fileName);
		output.flush();

		// read IP address of peer who contain file to download
		Object searchResultObject = input.readObject();

		if(searchResultObject instanceof String)
		{
			System.out.println("There is no such file exist !!!");
		}
		else
		{		
			@SuppressWarnings("unchecked")
			ArrayList<String> searchResults = (ArrayList<String>) searchResultObject;
			// we are picking up 1st Client/Server containg required file
			String searchResult=null;
			String[] ipAndPort = null; 
			System.out.println("File :" + fileName + "is available on");
			for (int i = 0; i < searchResults.size(); i++) {
				searchResult = searchResults.get(i);
				ipAndPort = searchResult.split(":", 2);
				System.out.println("IP ADDRESS: " + ipAndPort[0] + "\tPORT: "
						+ ipAndPort[1]);
			}

			String peerIPAddress = ipAndPort[0];
			int peerPortNumber = Integer.parseInt(ipAndPort[1]);
			fileTransfertoPeer(fileName, peerIPAddress, peerPortNumber);
			
		}
	}

	private static void registerPeer() throws IOException, Exception {
		// TODO Auto-generated method stub
		String SERVERPORT2 = (String) prop.get("indexServerPort");
		int SERVERPORT = Integer.parseInt(SERVERPORT2);
		String CLIENTPORT2 = (String) prop.get("peer");
		int CLIENTPORT = Integer.parseInt(CLIENTPORT2);
		String FOLDERURL = (String) prop.get("peerSourcefolderUrl");

		String CLIENTIPADDRESS=null;
		Enumeration en = NetworkInterface.getNetworkInterfaces();
	    while (en.hasMoreElements()) {
	        NetworkInterface i = (NetworkInterface) en.nextElement();
	        for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
	            InetAddress addr = (InetAddress) en2.nextElement();
	            if (!addr.isLoopbackAddress()) {
	                if (addr instanceof Inet4Address) {
	                	CLIENTIPADDRESS=addr.getHostAddress();
	                }	                
	            }
	        }
	    }
		
		String serverIP=(String)prop.get("serverIPAddress");
		Socket clientSocket = new Socket(serverIP, SERVERPORT);
		
		File folder = new File(FOLDERURL);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> list = new ArrayList<>();
		System.out.println("Below files got registered");
		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println("files:" + listOfFiles[i].getName());
			list.add(listOfFiles[i].getName());
		}
		ObjectOutputStream output = new ObjectOutputStream(
				clientSocket.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(
				clientSocket.getInputStream());

		output.writeObject("Register");
		output.flush();
		output.writeObject(CLIENTPORT);
		output.flush();
		
		output.writeObject(CLIENTIPADDRESS);
		output.flush();		
		
		Object portAck = input.readObject();
		output.writeObject(list);
		output.flush();
	}

	public static void fileTransfertoPeer(String fileName, String IPAddress,
			int portNumber) throws IOException, Exception {
		
		Socket clientSideSocket = null;
		prop.load(new FileInputStream(new File("config.properties")));
		String FOLDERURL = (String) prop.get("peerDownlaodfolderUrl");		
		try 
		{
			int bytesRead;
			clientSideSocket = new Socket(IPAddress, portNumber);
			
            DataOutputStream dos = new DataOutputStream(clientSideSocket.getOutputStream());
            dos.writeUTF(fileName);
			DataInputStream clientData = new DataInputStream(clientSideSocket.getInputStream());
			fileName=clientData.readUTF();
			OutputStream output=new FileOutputStream(FOLDERURL+"ReceivedFile_"+fileName);
			long size= clientData.readLong();
			byte [] buffer = new byte[1024];
			while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            clientData.close();            
            System.out.println("File "+fileName+" received from client.");			
		}
		catch(Exception e)
		{
			
		}
	}
}
