import java.awt.image.TileObserver;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class IndexMultiServerThread extends Thread {

	private Socket socket = null;	
	String peerIDLis = null;
	public static LinkedHashMap<String, ArrayList<String>> linkedHashMap = new LinkedHashMap<String, ArrayList<String>>();

	public IndexMultiServerThread() {
		// TODO Auto-generated constructor stub
	}

	public IndexMultiServerThread(Socket socket) {
		super("IndexMultiServerThread");
		this.socket = socket;
	}

	public void search(Object searchObject, ObjectOutputStream output) throws IOException, ClassNotFoundException {
		String filename=null;
		filename = (String) searchObject;
		
		if (linkedHashMap.get(filename) == null) {
			output.writeObject("There is no such file exist");
			output.flush();
		} 
		else {
			output.writeObject(linkedHashMap.get(filename));
			output.flush();
		}
	}

	public void run() {
		String purpose = null;
		try {
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());	
			Object action = input.readObject();	
			purpose = (String) action;			
			
			if(purpose.equals("Register"))
			{
				Object peerPort=input.readObject();
				int peerPortNum=(int) peerPort;
				output.writeObject("Server: Received port number");
				
				Object peerIPAddresss=input.readObject();
				String peerIPAddress=(String) peerIPAddresss;
				output.writeObject("Server: Received IPAddress number"+peerIPAddress);
				
				
				Object object = input.readObject();
				if(object!=null){
				{
					registerPeer(object,peerPortNum,peerIPAddress);
				}
				}
				else{
					System.out.println("Failed while creating object");
				}
			}
			if(purpose.equals("Search"))
			{
				output.writeObject("Searching.....");
				Object searchObject = input.readObject();
				search(searchObject,output);
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void registerPeer(Object input, int peerPortNum, String peerIPAddress) throws IOException, ClassNotFoundException
	{
		String addrs=peerIPAddress;
		String temp= Integer.toString(peerPortNum);
		String token= null; 
		token= addrs.concat(":");
		token = token.concat(temp);
		
		ArrayList<String> titleList = new ArrayList<String>();
		titleList = (ArrayList<String>) input;
		for (int i = 0; i < titleList.size(); i++) {
			String key=titleList.get(i);
			if(linkedHashMap.containsKey(key))
			{
				ArrayList<String> val= linkedHashMap.get(key);
				if(!val.contains(token))
				{
					val.add(token);
				}
			}
			else
			{
				ArrayList<String> values=new ArrayList<String>();
				values.add(token);
				linkedHashMap.put(titleList.get(i),values);
				
			}
		}
		System.out.println(linkedHashMap);
	}
	
}