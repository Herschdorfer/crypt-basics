package crypt_basics.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

	private List<Thread> threadList = new ArrayList<>();

	private List<ClientRunner> clientList = new ArrayList<>();

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
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			logger.info("server socket open, waiting for clients");
			while (serverSocket.isBound()) {
				Socket s1 = serverSocket.accept();

				/**
				 * Create a new thread for each client connection to receive messages from them
				 */
				logger.log(Level.INFO, "client is here {0}:{1}", new Object[] { s1.getInetAddress(), s1.getPort() });
				InputStream in = s1.getInputStream();
				OutputStream out = s1.getOutputStream();
				ClientRunner client = new ClientRunner(s1.getInetAddress(), s1.getPort(), in, out);
				Thread clientThread = new Thread(client);
				clientThread.start();

				// add the client thread to the list for later eg. shutdown
				threadList.add(clientThread);
				clientList.add(client);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "server socket error", e);
		} finally {
			shutdown();
		}
	}

	/**
	 * Shuts down the server and stops the client threads.
	 */
	private void shutdown() {
		for (Thread t : threadList) {
			t.interrupt();
		}
		threadList.clear();
	}

	/**
	 * Disconnects a client.
	 *
	 * @param client the client to disconnect
	 */
	void disconnect(ClientRunner client) {
		clientList.remove(client);
	}

	/**
	 * The ClientRunner class represents a runnable task that receives messages from
	 * a client and adds them to the message queue.
	 */
	class ClientRunner implements Runnable {
		private InputStream in;
		private OutputStream out;
		private InetAddress inetAddress;
		private int port;

		/**
		 * Constructs a new ClientRunner object.
		 *
		 * @param inetAddress the IP address of the client
		 * @param i           the port number of the client
		 * @param in          the input stream to read the message from
		 * @param out
		 */
		public ClientRunner(InetAddress inetAddress, int i, InputStream in, OutputStream out) {
			this.inetAddress = inetAddress;
			this.port = i;
			this.in = in;
			this.out = out;
		}

		@Override
		public void run() {
			receive(in);
		}

		public InetAddress getInetAddress() {
			return inetAddress;
		}

		public int getPort() {
			return port;
		}

		/**
		 * Sends a message to the client.
		 *
		 * @param message the message to send
		 */
		public void send(String message) {
			try {
				out.write(message.getBytes());
			} catch (IOException e) {
				logger.log(Level.SEVERE, "client socket error", e);
			}
		}

		/**
		 * Receives a message from the client and adds it to the message queue.
		 *
		 * @param stream the input stream to read the message from
		 * @throws IOException if an I/O exception occurs while receiving the message
		 */
		private void receive(InputStream stream) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			try {
				while ((bytesRead = stream.read(buffer)) != -1) {
					String msg = new String(buffer, 0, bytesRead);
					msgQueue.add(msg);
					logger.log(Level.INFO,
							() -> String.format("(%s, %s): Received message: %s", inetAddress, port, msg));
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, "client socket error", e);
			}

			disconnect(this);
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

	public List<ClientRunner> getClientList() {
		return clientList;
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server(8080);
		server.openConnection();
	}
}
