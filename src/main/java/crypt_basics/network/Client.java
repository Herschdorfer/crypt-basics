package crypt_basics.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * The Class Client.
 */
public class Client {
	
    /** The logger. */	
	Logger logger = Logger.getLogger(Client.class.getName());

	/** The host. */
	private String host;

	/** The port. */
	private int port;

	/**
	 * Client constructor
	 */
	public Client(int port, String host) {
		this.port = port;
		this.host = host;
	}

	/**
	 * Send message.
	 */
	public void send(String message) throws IOException {
		OutputStream out;
		// open TCP Socket
		try (Socket socket = new Socket(host, port)) {
			out = socket.getOutputStream();
			out.write(message.getBytes());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
}
