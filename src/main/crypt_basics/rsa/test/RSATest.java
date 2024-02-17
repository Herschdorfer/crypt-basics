package crypt_basics.rsa.test;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.Test;

import crypt_basics.Algorithm;
import crypt_basics.rsa.RSA;

class RSATest {

	@Test
	void testRSA() {

		Algorithm sut = new RSA(BigInteger.probablePrime(8, new Random()), BigInteger.probablePrime(8, new Random()),
				BigInteger.probablePrime(8, new Random()));

		char[] m = "Hello World".toCharArray();

		char[] c = sut.encrypt(m);
		System.out.println(c);
		m = sut.decrypt(c);
		System.out.println(m);

		assert (new String(m).equals("Hello World"));
	}
}
