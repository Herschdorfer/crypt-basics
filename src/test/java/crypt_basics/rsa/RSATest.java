package crypt_basics.rsa;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.Test;

class RSATest {

	void generalTest(RSA sut) {
		char[] m = "Hello World".toCharArray();

		BigInteger[] c = sut.encrypt(m);
		m = sut.decrypt(c);
		assert (new String(m).equals("Hello World"));
	}

	/**
	 * Test RSA with 20 bits
	 */
	@Test
	void testRSA10Bit() {
		RSA sut = new RSA(BigInteger.probablePrime(10, new Random()), BigInteger.probablePrime(10, new Random()),
				BigInteger.probablePrime(10, new Random()));

		generalTest(sut);
	}

	/**
	 * Test RSA with 24 bits
	 */
	@Test
	void testRSA12Bit() {
		RSA sut = new RSA(BigInteger.probablePrime(12, new Random()), BigInteger.probablePrime(12, new Random()));

		generalTest(sut);
	}

	/**
	 * Test RSA with 32 bits
	 */
	@Test
	void testRSA16Bit() {
		RSA sut = new RSA(BigInteger.probablePrime(16, new Random()), BigInteger.probablePrime(16, new Random()),
				BigInteger.probablePrime(16, new Random()));

		generalTest(sut);
	}

	/**
	 * Test RSA with 2048 bits
	 */
	@Test
	void testRSA2048Bit() {
		RSA sut = new RSA(BigInteger.probablePrime(1024, new Random()), BigInteger.probablePrime(1024, new Random()));

		generalTest(sut);
	}

	/**
	 * Test RSA with 4096 bits
	 */
	@Test
	void testRSA4096Bit() {
		RSA sut = new RSA(BigInteger.probablePrime(2048, new Random()), BigInteger.probablePrime(2048, new Random()));

		generalTest(sut);
	}
}
