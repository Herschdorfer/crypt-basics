package crypt_basics.network;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClientServerTest {

	/**
	 * Test communication between client and server
	 */
	@Test
	void testCommunication() {
		Server server = new Server(8080);
		Client client = new Client(8080, "localhost");

		assertDoesNotThrow(() -> {
			server.openConnection();
			Thread.sleep(100);

			client.start();
			client.send("Hello World");
			Thread.sleep(100);
			assertEquals("Hello World", server.getMsgQueue().poll());

			server.getClientList().get(0).send("Hello World");

			Thread.sleep(100);

			client.shutdown();
			server.close();

		});
	}
}
