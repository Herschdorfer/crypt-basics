package crypt_basics.aes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestSymCrypt {

	/**
	 * AES CBC NoPadding
	 */
	@Test
	void testCBCNoPadding() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/CBC/NoPadding");

			String shortText = "1234567890123456";

			byte[] temp = sut.encrypt(shortText);
			assertEquals(shortText, sut.decrypt(temp));
		});
	}

	/**
	 * AES CBC PKCS5Padding
	 */
	@Test
	void testCBCPadding() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/CBC/PKCS5Padding");

			String shortText = "123456789012345";

			byte[] temp = sut.encrypt(shortText);
			assertEquals(shortText, sut.decrypt(temp));
		});
	}

	/**
	 * AES ECB NoPadding
	 */
	@Test
	void testECBNoPadding() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/ECB/NoPadding");

			String shortText = "1234567890123456";

			byte[] temp = sut.encrypt(shortText);
			assertEquals(shortText, sut.decrypt(temp));
		});
	}

	/**
	 * AES ECB PKCS5Padding
	 */
	@Test
	void testECBPadding() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/ECB/PKCS5Padding");

			String shortText = "123456789012345";

			byte[] temp = sut.encrypt(shortText);
			assertEquals(shortText, sut.decrypt(temp));
		});
	}

	/**
	 * AES CBC PKCS5Padding
	 * 
	 * with long text
	 */
	@Test
	void testLong() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/CBC/PKCS5Padding");

			String longString = """
					Lorem Ipsum is simply dummy text of the printing and typesetting industry.
					Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
					when an unknown printer took a galley of type and scrambled it to make a
					type specimen book. It has survived not only five centuries, but also the
					leap into electronic typesetting, remaining essentially unchanged. It was
					popularised in the 1960s with the release of Letraset sheets containing
					Lorem Ipsum passages, and more recently with desktop publishing software
					like Aldus PageMaker including versions of Lorem Ipsum.
					""";

			byte[] temp = sut.encrypt(longString);
			assertEquals(longString, sut.decrypt(temp));
		});
	}

}
