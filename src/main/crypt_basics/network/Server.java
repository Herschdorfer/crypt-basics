package crypt_basics.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * The Class Server.
 */
public class Server {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Server.class.getName());

	/** The port. */
	private int port;

	/**
	 * Server constructor
	 */
	Server(int port) {
		this.port = port;
	}

	/**
	 * Open connection.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void openConnection() throws IOException {
		// open TCP Socket
		try (ServerSocket serversocket = new ServerSocket(port)) {

			logger.info("server socket open, waiting for clients");
			while (serversocket.isBound()) {
				Socket s1 = serversocket.accept();
				logger.info("client  is here");

				InputStream stream = s1.getInputStream();

				while (s1.isConnected()) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					try {
						while ((bytesRead = stream.read(buffer)) != -1) {
							System.out.println(new String(buffer, 0, bytesRead));
						}
					} catch (IOException e) {
						logger.info("disconnect");
						s1.close();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server(8080);
		server.openConnection();
	}
}
