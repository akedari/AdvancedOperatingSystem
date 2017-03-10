import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

public class PeerFunctions extends Thread {

	public static Properties prop = new Properties();
	public static LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
	public Socket socket = null;
	ObjectInputStream input;
	ObjectOutputStream output;
	boolean result;
	String getResult= new String();

	public PeerFunctions(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public void run() {
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			String[] operation = new String[2];
			operation[0] = new String();
			operation[1] = new String();
			while (true) {
				Object t1 = input.readObject();
				String block = (String) t1;
				operation = block.split(":");				

				switch (operation[0]) {
				case "0":
					result = put(operation[1],operation[2]);
					output.writeObject(result);
					output.flush();
					break;
				case "1":
					getResult=get(operation[1]);
					output.writeObject(getResult);
					output.flush();
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();			
			System.out.println("Server down !!!");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String get(String operation) throws IOException {
		// TODO Auto-generated method stub
		String key = operation;

		if (linkedHashMap.containsKey(key)) {
			String value = linkedHashMap.get(key);
			return value;
		}
		else
		{
			String value2="No value associated with key";
			return value2;
		}
	}

	private boolean put(String filename, String operation) throws NumberFormatException, IOException, Exception {
		// TODO Auto-generated method stub
		//String KeyValue = filename;
		int i;
		String temp;
		String[] temp1;
		String[] ipAndPort,hashIpPort;
		prop.load(new FileInputStream(new File("config.properties")));
		String ip = (String) prop.get("ip");
		String port = (String) prop.get("peer");
		//System.out.println("\n ip="+ip+"  port="+port);		
		
		temp1=operation.split("#");
		
		for (i = 0; i < temp1.length; i++) {
			if (linkedHashMap.containsKey(filename)) { 			//if(i!=1)
				//ipAndPort = temp1[i].split("@");
				//if (! (ip==ipAndPort[0]) && (port==ipAndPort[1])) {
				if(i!=1){
					temp = linkedHashMap.get(filename);
					if (!temp.contains(temp1[i])) {
						temp = temp + "#" + temp1[i]; // $
						linkedHashMap.put(filename, temp);
					}
				}
			} else {				
				linkedHashMap.put(filename, temp1[i]);
				ipAndPort = temp1[i].split("@");
				hashIpPort = temp1[1].split("@");
				
				if(! ((ip.equalsIgnoreCase(hashIpPort[0])) && (port.equalsIgnoreCase(hashIpPort[1]))))
				{
					//System.out.println("\n Haship="+hashIpPort[0]+"  Hashport="+hashIpPort[1]);
					filedownload(filename, ipAndPort[0],
					Integer.parseInt(ipAndPort[1]));
				}
			}
		}
		//System.out.println(linkedHashMap);
		return true;

	}
	
	private static void filedownload(String fileName, String IPAddress,
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
        //clientData.close();            
        //System.out.println("File "+fileName+" received from peer.");			
	}
	catch(Exception e){
	}
		System.out.println("File downloaded successfully !!!");	
}
	
}
