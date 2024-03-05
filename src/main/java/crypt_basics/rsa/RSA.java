package crypt_basics.rsa;

import java.math.BigInteger;

/**
 * The RSA class represents the RSA encryption and decryption algorithm. It
 * provides methods for generating RSA keys, encrypting and decrypting messages.
 */
public class RSA {
	private RSAKeyGen keyGen;

	/**
	 * Constructs an RSA object with the given prime numbers and public exponent.
	 * The private key is generated automatically.
	 *
	 * @param p the first prime number
	 * @param q the second prime number
	 * @param e the public exponent
	 */
	public RSA(BigInteger p, BigInteger q, BigInteger e) {
		this.keyGen = new RSAKeyGen(p, q, e);
		this.keyGen.generatePrivateKey();
	}

	/**
	 * Constructs an RSA object with randomly generated prime numbers of the
	 * specified bit length and the given public exponent. The private key is
	 * generated automatically.
	 *
	 * @param bits the bit length of the prime numbers
	 * @param e    the public exponent
	 */
	public RSA(int bits, BigInteger e) {
		this.keyGen = new RSAKeyGen(BigInteger.probablePrime(bits, new java.util.Random()),
				BigInteger.probablePrime(bits, new java.util.Random()), e);
		this.keyGen.generatePrivateKey();
	}

	/**
	 * Constructs an RSA object with randomly generated prime numbers of the
	 * specified bit length and the default public exponent (65537). The private key
	 * is generated automatically.
	 *
	 * @param bits the bit length of the prime numbers
	 */
	public RSA(int bits) {
		this.keyGen = new RSAKeyGen(BigInteger.probablePrime(bits, new java.util.Random()),
				BigInteger.probablePrime(bits, new java.util.Random()), BigInteger.valueOf(65537));
		this.keyGen.generatePrivateKey();
	}

	/**
	 * Encrypts the given message using the RSA algorithm.
	 *
	 * @param m the message to be encrypted
	 * @return an array of encrypted values
	 */
	public BigInteger[] encrypt(char[] m) {
		BigInteger[] result = new BigInteger[m.length];
		for (int i = 0; i < m.length; i++) {
			result[i] = BigInteger.valueOf(m[i]).modPow(keyGen.getE(), keyGen.getN());
		}
		return result;
	}

	/**
	 * Decrypts the given cipher text using the RSA algorithm.
	 *
	 * @param c the cipher text to be decrypted
	 * @return an array of decrypted characters
	 */
	public char[] decrypt(BigInteger[] c) {
		char[] result = new char[c.length];
		for (int i = 0; i < c.length; i++) {
			result[i] = (char) (c[i].modPow(keyGen.getD(), keyGen.getN()).intValue());
		}
		return result;
	}
}
