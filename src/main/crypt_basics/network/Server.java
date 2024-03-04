package crypt_basics.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class Server.
 */
public class Server {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Server.class.getName());

	/** The port. */
	private int port;

	/** The message queue. */
	private Queue<String> msgQueue = new PriorityQueue<>();

	/** the server thread */
	private Thread serverThread;

	/**
	 * Server constructor
	 */
	public Server(int port) {
		this.port = port;
	}

	/**
	 * Open connection.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void openConnection() throws IOException {
		serverThread = new Thread(this::run);

		serverThread.start();
	}

	/**
	 * Run the main server loop.
	 */
	private void run() {
		// open TCP Socket
		try (ServerSocket serversocket = new ServerSocket(port)) {

			logger.info("server socket open, waiting for clients");
			while (serversocket.isBound()) {
				Socket s1 = serversocket.accept();
				logger.info("client  is here");

				InputStream stream = s1.getInputStream();

				while (s1.isConnected()) {
					receive(s1, stream);
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "server socket error", e);
		}
	}

	/**
	 * Receive.
	 *
	 * @param socket the socket
	 * @param stream the stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void receive(Socket socket, InputStream stream) throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		try {
			while ((bytesRead = stream.read(buffer)) != -1) {
				String msg = new String(buffer, 0, bytesRead);
				msgQueue.add(msg);
				logger.log(Level.INFO, msg);
			}
		} catch (IOException e) {
			logger.info("disconnect");
			socket.close();
		}
	}

	/**
	 * Gets the message queue.
	 *
	 * @return the message queue
	 */
	public Queue<String> getMsgQueue() {
		return msgQueue;
	}

	/**
	 * Close the server
	 */
	public void close() {
		serverThread.interrupt();
	}
}
