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

	@Test
	void generationTest2048() throws NoSuchAlgorithmException {
		DSA sut = new DSA(2048, 256);
		generalTest(sut);
	}

	@Test
	void generationTest3072() throws NoSuchAlgorithmException {
		DSA sut = new DSA(3072, 256);
		generalTest(sut);
	}

	@Test
	void invalidNBitsTest() {
		assertThrows(IllegalArgumentException.class, () -> new DSA(2048, 1234));
	}

	@Test
	void invalidNBitsForL2048Test() {
		assertThrows(IllegalArgumentException.class, () -> new DSA(2048, 512));
	}

	@Test
	void invalidNBitsForL3072Test() {
		assertThrows(IllegalArgumentException.class, () -> new DSA(3072, 128));
	}
}