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
 * The Server class represents a server that listens for incoming client
 * connections and receives messages from the clients.
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
	 * Constructs a Server object with the specified port.
	 *
	 * @param port the port number to listen on
	 */
	public Server(int port) {
		this.port = port;
	}

	/**
	 * Opens the server connection and starts listening for client connections.
	 *
	 * @throws IOException if an I/O exception occurs while opening the connection
	 */
	public void openConnection() throws IOException {
		serverThread = new Thread(this::run);
		serverThread.start();
	}

	/**
	 * Runs the main server loop, accepting client connections and receiving
	 * messages.
	 */
	private void run() {
		// open TCP Socket
		try (ServerSocket serversocket = new ServerSocket(port)) {
			logger.info("server socket open, waiting for clients");
			while (serversocket.isBound()) {
				Socket s1 = serversocket.accept();
				logger.info("client is here");
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
	 * Receives a message from the client and adds it to the message queue.
	 *
	 * @param socket the client socket
	 * @param stream the input stream to read the message from
	 * @throws IOException if an I/O exception occurs while receiving the message
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
	 * Closes the server and stops listening for client connections.
	 */
	public void close() {
		serverThread.interrupt();
	}
}
