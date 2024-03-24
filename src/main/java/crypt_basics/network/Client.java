package crypt_basics.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
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

	private boolean shutdown;

	private OutputStream out;

	private InputStream in;

	private Thread reader;

	private Socket socket;

	private Thread writer;

	/**
	 * Client constructor
	 * 
	 * @param port the port number to connect to
	 * @param host the host address to connect to
	 */
	public Client(int port, String host) {
		this.port = port;
		this.host = host;
		this.shutdown = false;
	}

	public void shutdown() throws IOException {
		this.shutdown = true;
		this.reader.interrupt();
		this.writer.interrupt();
		this.socket.close();
	}

	/**
	 * Starts the client.
	 * 
	 * @throws IOException if an I/O error occurs while sending
	 */
	public void start() throws IOException {
		// open TCP Socket
		try {
			socket = new Socket(host, port);
			out = socket.getOutputStream();
			in = socket.getInputStream();
			reader = new Thread(new Reader());
			writer = new Thread(new Writer());

			reader.start();
			writer.start();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	public void send(String msg) {
		try {
			out.write(msg.getBytes());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	private class Reader implements Runnable {

		@Override
		public void run() {
			// read from input stream
			byte[] buffer = new byte[1024];
			int bytesRead;
			try {
				while (!shutdown && (bytesRead = in.read(buffer)) != -1) {
					String msg = new String(buffer, 0, bytesRead);
					logger.log(Level.INFO, () -> String.format("Received message: %s", msg));
				}
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}

	}

	private class Writer implements Runnable {

		@Override
		public void run() {
			BufferedReader locReader = new BufferedReader(
					new InputStreamReader(System.in));
			while (!shutdown) {
				System.err.println("Enter message: ");

				try {
					send(locReader.readLine());
				} catch (IOException e) {
					logger.severe(e.getMessage());
					shutdown = true;
				}
			}
		}

	}
}
