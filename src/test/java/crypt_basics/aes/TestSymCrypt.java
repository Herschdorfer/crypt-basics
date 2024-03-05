package crypt_basics.aes;

import static org.junit.jupiter.api.Assertions.*;

import java.security.SecureRandom;

import org.junit.jupiter.api.Test;

import crypt_basics.aes.SymCrypt;

class TestSymCrypt {

	@Test
	void test() {
		assertDoesNotThrow(() -> {
			SymCrypt sut = new SymCrypt("password", "AES/CBC/PKCS5Padding");
			SecureRandom iv = new SecureRandom();
			byte[] temp = sut.encrypt("hello world", iv);
			assertEquals(sut.decrypt(temp, iv), "hello world");
		});
	}

}
