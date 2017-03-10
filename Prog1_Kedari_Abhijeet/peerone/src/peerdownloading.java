import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

public class peerdownloading extends Thread {

	public static Properties prop = new Properties();

	public Socket socket = null;

	public peerdownloading(Socket socket) {
		this.socket = socket;
	}

	public void run() {

		try {
			prop.load(new FileInputStream("config.properties"));
			String FOLDERURL = (String) prop.get("peerSourcefolderUrl");
			
			DataInputStream clientSocket = new DataInputStream(socket.getInputStream());
			String fileName=clientSocket.readUTF();
			String temp=FOLDERURL+fileName;
			File file = new File(temp);
			byte[] fileByteArray = new byte[(int) file.length()];
			
			FileInputStream fileIS = new FileInputStream(file);
            BufferedInputStream bufferIS = new BufferedInputStream(fileIS);
            DataInputStream dataIS = new DataInputStream(bufferIS);
            dataIS.readFully(fileByteArray,0,fileByteArray.length);
            
            OutputStream outputST = socket.getOutputStream();
            DataOutputStream dataOS = new DataOutputStream(outputST);
            dataOS.writeUTF(file.getName());
            dataOS.writeLong(fileByteArray.length);
            dataOS.write(fileByteArray, 0, fileByteArray.length);
            dataOS.flush();
            System.out.println("File "+fileName+" sent to client.");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
}
