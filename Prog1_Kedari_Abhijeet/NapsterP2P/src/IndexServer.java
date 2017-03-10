
import java.net.ServerSocket;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IndexServer {

	public static Properties prop= new Properties();
	
	public static void main(String[] args) throws IOException {
		
		prop.load(new FileInputStream(new File("config.properties")));
		String indexServerport = (String)prop.get("indexServerPort");
		System.out.println("IndexServer listening on Port:"+indexServerport);
		int SERVERPORT=Integer.parseInt(indexServerport);
		boolean listing = true;
		try (ServerSocket indexServerSocket = new ServerSocket(SERVERPORT)) {
			while (listing) {
				new IndexMultiServerThread(indexServerSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println("Could not listen on server port");
			System.exit(-1);
		}
	}

}
