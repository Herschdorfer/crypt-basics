package crypt_basics.network;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class ClientTest {

    @Test
    void testStartNoServer() throws IOException {
        Client client = new Client(8080, "localhost");

        assertDoesNotThrow(() -> {
            client.start();
            assertFalse(client.isConnected());
            client.shutdown();
        });
    }
}