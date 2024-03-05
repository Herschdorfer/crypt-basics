package crypt_basics.network;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import crypt_basics.network.Client;
import crypt_basics.network.Server;

/**
 * Test the client server communication
 */
class ClientServerTest {

	/**
	 * Test the client server communication
	 */
	@Test
	void test() {
		Server server = new Server(8080);
		Client client = new Client(8080, "localhost");

		assertDoesNotThrow(() -> {
			server.openConnection();
			Thread.sleep(100);

			client.send("Hello World");
			Thread.sleep(100);
			assertEquals("Hello World", server.getMsgQueue().poll());
		});
	}
}
