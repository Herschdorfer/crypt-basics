package crypt_basics.rsa.test;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.Test;

import crypt_basics.Algorithm;
import crypt_basics.rsa.RSA;

class RSATest {

	void generalTest(Algorithm sut) {
		char[] m = "Hello World".toCharArray();

		BigInteger[] c = sut.encrypt(m);
		System.out.println(c);
		m = sut.decrypt(c);
		System.out.println(m);

		assert (new String(m).equals("Hello World"));
	}
	
	@Test
	void testRSA8Bit() {
		Algorithm sut = new RSA(BigInteger.probablePrime(8, new Random()), BigInteger.probablePrime(8, new Random()),
				BigInteger.probablePrime(8, new Random()));

        generalTest(sut);
	}
	
	@Test
	void testRSA10Bit() {
		Algorithm sut = new RSA(BigInteger.probablePrime(8, new Random()), BigInteger.probablePrime(10, new Random()),
				BigInteger.probablePrime(10, new Random()));

		generalTest(sut);
	}
	
	@Test
	void testRSA12Bit() {
		Algorithm sut = new RSA(BigInteger.probablePrime(12, new Random()), BigInteger.probablePrime(12, new Random()),
				BigInteger.probablePrime(12, new Random()));

		generalTest(sut);
	}
	
	@Test
	void testRSA16Bit() {
		Algorithm sut = new RSA(BigInteger.probablePrime(16, new Random()), BigInteger.probablePrime(16, new Random()),
				BigInteger.probablePrime(16, new Random()));

		generalTest(sut);
	}
}
