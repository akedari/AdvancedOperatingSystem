import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class PeerOneThreads extends Thread {

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
		String CLIENTPORT2 = (String) prop.get("peer");
		int CLIENTPORT = Integer.parseInt(CLIENTPORT2);

		boolean listing = true;
		try (ServerSocket peerSocket = new ServerSocket(CLIENTPORT)) {
			while (listing) {
				new peerdownloading(peerSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port:" + CLIENTPORT);
			System.exit(-1);
		}

	}

}
