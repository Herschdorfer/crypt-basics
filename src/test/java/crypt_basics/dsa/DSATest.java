package crypt_basics.dsa;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

class DSATest {

	void generalTest(DSA sut) {
		BigInteger[] signature = sut.sign("hello".toCharArray());

		assert (sut.verify(signature, "hello".toCharArray()));
	}

	/**
	 * Test generation of DSA key pair with 1024 bits
	 */
	@Test
	void generationTest2048() throws NoSuchAlgorithmException {
		DSA sut = new DSA(2048, 256);
		generalTest(sut);
	}

	/**
	 * Test generation of DSA key pair with 3072 bits
	 */
	@Test
	void generationTest3072() throws NoSuchAlgorithmException {
		DSA sut = new DSA(3072, 256);
		generalTest(sut);
	}

	/**
	 * Test generation of DSA key pair with 4096 bits but invalid L
	 */
	@Test
	void invalidNBitsTest() {
		assertThrows(IllegalArgumentException.class, () -> new DSA(2048, 1234));
	}

	/**
	 * Test generation of DSA key pair with 2048 bits but invalid L
	 */
	@Test
	void invalidNBitsForL2048Test() {
		assertThrows(IllegalArgumentException.class, () -> new DSA(2048, 512));
	}

	/**
	 * Test generation of DSA key pair with 3072 bits but invalid L
	 */
	@Test
	void invalidNBitsForL3072Test() {
		assertThrows(IllegalArgumentException.class, () -> new DSA(3072, 128));
	}
}