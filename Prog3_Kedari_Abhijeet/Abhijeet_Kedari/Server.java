import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public class Server extends Thread{

	public static Properties prop = new Properties();
	Socket socket = null;

	public void run() {
		try 
		{
			prop.load(new FileInputStream(new File("config.properties")));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		String peerports = (String) prop.get("port");
		int peerport = Integer.parseInt(peerports);

		boolean listing = true;
		try (ServerSocket peerSocket = new ServerSocket(peerport)) {
			while (listing) {
				//System.out.println("Server_Client is up !!!");
				new PeerFunctions(peerSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port:" + peerport);
			System.exit(-1);
		}

	}
}
