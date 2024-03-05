package crypt_basics.rsa;

import java.math.BigInteger;

/**
 * The RSAKeygen class is responsible for generating RSA key pairs.
 */
public class RSAKeygen {

	private BigInteger n;
	private BigInteger phiN;
	private BigInteger e;
	private BigInteger d;

	/**
	 * Constructs an RSAKeygen object with the given prime numbers p and q, and the
	 * public exponent e.
	 *
	 * @param p the first prime number
	 * @param q the second prime number
	 * @param e the public exponent
	 */
	public RSAKeygen(BigInteger p, BigInteger q, BigInteger e) {
		n = p.multiply(q);
		phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		this.e = e;
	}

	/**
	 * Generates the private key using the public exponent and the totient of n.
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
