package crypt_basics.hmac;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class HMACTest {

	@Test
	void testVerify() {
		assertDoesNotThrow(() -> {
			HMAC hmac = null;
			hmac = new HMAC("key".getBytes());
			assert (hmac.verify("message".getBytes(), hmac.calc("message".getBytes())));
		});
	}
}
