/**
 * 
 */
package crypt_basics.rsa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class KeyGenTest {

	@Test
	void testRsaPrivateKeyGen() {
		RSAKeyGen sut = new RSAKeyGen(BigInteger.probablePrime(8, new Random()),
				BigInteger.probablePrime(100, new Random()), BigInteger.valueOf(65537));
		BigInteger d = sut.generatePrivateKey();

		char[] chars = "Hello World".toCharArray();
		BigInteger[] encrypted = new BigInteger[chars.length];
		BigInteger[] decrypted = new BigInteger[chars.length];
		for (int i = 0; i < chars.length; i++) {
			encrypted[i] = BigInteger.valueOf(chars[i]).modPow(BigInteger.valueOf(65537), sut.getN());
			decrypted[i] = encrypted[i].modPow(d, sut.getN());
		}

		for (int i = 0; i < chars.length; i++) {
			assertEquals(chars[i], (char) decrypted[i].intValue());
		}

	}

}
