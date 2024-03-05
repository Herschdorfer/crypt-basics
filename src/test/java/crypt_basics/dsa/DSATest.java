package crypt_basics.dsa;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

import crypt_basics.dsa.DSA;

class DSATest {

	void generalTest(DSA sut) {
		BigInteger[] signature = sut.sign("hello".toCharArray());

		assert (sut.verify(signature, "hello".toCharArray()));
	}

	@Test
	void generationTest() throws NoSuchAlgorithmException {
		DSA sut = new DSA(1024, 160);
		generalTest(sut);
	}

}
