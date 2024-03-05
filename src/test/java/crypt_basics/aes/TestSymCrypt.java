package crypt_basics.aes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.SecureRandom;

import javax.crypto.spec.IvParameterSpec;

import org.junit.jupiter.api.Test;

class TestSymCrypt {

	private IvParameterSpec createIv() {
		SecureRandom random = new SecureRandom();
		byte[] ivBytes = new byte[16];
		random.nextBytes(ivBytes);
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		return iv;
	}
	
	@Test
	void testNoPadding() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/CBC/NOPADDING");

			String shortText = "1234567890123456";
			
			IvParameterSpec iv = createIv();
			byte[] temp = sut.encrypt(shortText, iv);
			assertEquals(shortText, sut.decrypt(temp, iv));
		});
	}
	
	void testPadding() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/CBC/PKCS5Padding");

			String shortText = "12345678901234566";
			
			IvParameterSpec iv = createIv();
			byte[] temp = sut.encrypt(shortText, iv);
			assertEquals(shortText, sut.decrypt(temp, iv));
		});
	}

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

			IvParameterSpec iv = createIv();
			byte[] temp = sut.encrypt(longString, iv);
			assertEquals(longString, sut.decrypt(temp, iv));
		});
	}

}
