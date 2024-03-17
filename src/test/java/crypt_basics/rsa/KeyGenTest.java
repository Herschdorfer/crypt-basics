package crypt_basics.rsa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.Test;

class KeyGenTest {

	@Test
	void testRsaPrivateKeyGen() {
		RSAKeyGen sut = new RSAKeyGen(BigInteger.probablePrime(8, new Random()),
				BigInteger.probablePrime(8, new Random()), BigInteger.valueOf(65537));
		BigInteger d = sut.generatePrivateKey();

		for (int i = 0; i < 100; i++) {
			BigInteger c = BigInteger.valueOf(i).modPow(d, sut.getN());
			BigInteger m = c.modPow(sut.getE(), sut.getN());

			assertEquals(BigInteger.valueOf(i), m);
		}

	}

}
