package crypt_basics.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * The Class Client.
 */
public class Client {
	
	/** The host. */
	private String host;
	
	/** The port. */
	private int port;

	/**
	 * Client constructor
	 */
	Client(int port, String host) {
		this.port = port;
		this.host = host;
	}

	/**
	 * Open connection.
	 */
	public void openConnection() {
		// open TCP Socket
		try (Socket socket = new Socket(host, port)) {
			OutputStream out = socket.getOutputStream();
			out.write("Hello Server".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Client client = new Client(8080, "localhost");
		client.openConnection();
	}
}
