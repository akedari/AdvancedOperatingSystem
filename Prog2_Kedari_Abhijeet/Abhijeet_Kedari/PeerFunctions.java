import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
					result = put(operation[1]);
					output.writeObject(result);
					output.flush();
					break;
				case "1":
					getResult=get(operation[1]);
					output.writeObject(getResult);
					output.flush();
					break;
				case "2":
					result = del(operation[1]);
					output.writeObject(result);
					output.flush();
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean del(String operation) throws IOException {
		// TODO Auto-generated method stub
		String key = operation;
		if (linkedHashMap.containsKey(key)) {
			String value = linkedHashMap.remove(key);
			return true;
		} else {
			return false;
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

	private boolean put(String operation) {
		// TODO Auto-generated method stub
		String[] KeyValue = operation.split("#");
		linkedHashMap.put(KeyValue[0], KeyValue[1]);
		//System.out.println(linkedHashMap);
		return true;

	}

}
