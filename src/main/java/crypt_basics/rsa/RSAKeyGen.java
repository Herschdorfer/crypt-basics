package crypt_basics.rsa;

import java.math.BigInteger;

/**
 * The RSAKeyGen class is responsible for generating RSA key pairs.
 */
public class RSAKeyGen {

	private BigInteger n;
	private BigInteger phiN;
	private BigInteger e;
	private BigInteger d;

	/**
	 * Constructs an RSAKeyGen object with the given prime numbers p and q, and the
	 * public exponent e.
	 *
	 * @param p the first prime number
	 * @param q the second prime number
	 * @param e the public exponent
	 */
	public RSAKeyGen(BigInteger p, BigInteger q, BigInteger e) {
		n = p.multiply(q);
		phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		this.e = e;
	}

	/**
	 * Constructs an RSAKeyGen object with the given prime numbers p and q.
	 * 
	 * The public exponent e is set to 65537.
	 *
	 * @param p the first prime number
	 * @param q the second prime number
	 */
	public RSAKeyGen(BigInteger p, BigInteger q) {
		new RSAKeyGen(p, q, BigInteger.valueOf(65537));
	}

	/**
	 * Generates the private key using the public exponent and the Phi(n) value.
	 *
	 * @return the private key
	 */
	public BigInteger generatePrivateKey() {
		this.d = e.modInverse(phiN);
		return d;
	}

	/**
	 * Returns the modulus n.
	 *
	 * @return the modulus n
	 */
	public BigInteger getN() {
		return n;
	}

	/**
	 * Returns the public exponent e.
	 *
	 * @return the public exponent e
	 */
	public BigInteger getE() {
		return e;
	}

	/**
	 * Returns the private key d.
	 *
	 * @return the private key d
	 */
	public BigInteger getD() {
		return d;
	}
}
