package crypt_basics.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * The Client class represents a client that can send messages to a server.
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
	 * 
	 * @param port the port number to connect to
	 * @param host the host address to connect to
	 */
	public Client(int port, String host) {
		this.port = port;
		this.host = host;
	}

	/**
	 * Sends a message to the server.
	 * 
	 * @param message the message to send
	 * @throws IOException if an I/O error occurs while sending the message
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
