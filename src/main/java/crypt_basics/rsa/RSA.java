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
	}

	/**
	 * Constructs an RSA object with the given prime numbers. The public exponent is
	 * set to 65537 and the private key is generated automatically.
	 *
	 * @param p the first prime number
	 * @param q the second prime number
	 */
	public RSA(BigInteger p, BigInteger q) {
		this.keyGen = new RSAKeyGen(p, q, BigInteger.valueOf(65537));
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
