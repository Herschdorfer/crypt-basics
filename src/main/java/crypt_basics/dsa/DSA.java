package crypt_basics.dsa;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Logger;

import crypt_basics.hash.Hash;

/**
 * This class represents the Digital Signature Algorithm (DSA). It provides
 * methods for generating keys, signing messages, and verifying signatures.
 */
public class DSA {

	/** The Logger */
	Logger logger = Logger.getLogger(DSA.class.getName());

	private BigInteger x;
	private BigInteger y;
	private BigInteger q;
	private BigInteger p;
	private BigInteger g;

	private Hash hash;

	/**
	 * Constructor
	 * 
	 * @param lBits the number of bits of the prime number
	 * @param nBits the number of bits of the hash function
	 */
	public DSA(int lBits, int nBits) {

		final String INVALID_N_BITS_ERROR = "Invalid number of bits for n.";

		if (lBits == 2048) {
			if (nBits != 224 && nBits != 256) {
				throw new IllegalArgumentException(INVALID_N_BITS_ERROR);
			}
		} else if (lBits == 3072) {
			if (nBits != 256) {
				throw new IllegalArgumentException(INVALID_N_BITS_ERROR);
			}
		} else {
			throw new IllegalArgumentException("Invalid number of bits for l.");
		}

		try {
			this.hash = new Hash(nBits);
		} catch (NoSuchAlgorithmException e) {
			logger.severe(e.getMessage());
			System.exit(1);
		}

		calculatePQ(lBits, nBits);
		calculateG();
		generateKeys();
	}

	/**
	 * Generate the private and public keys
	 */
	private void generateKeys() {

		SecureRandom random = new SecureRandom();
		do {
			x = new BigInteger(q.bitLength(), random);
		} while (x.compareTo(BigInteger.ZERO) == 0 || x.compareTo(q) >= 0);

		y = g.modPow(x, p);
	}

	/**
	 * Calculate the generator g. g = h^((p-1)/q) mod p
	 */
	private void calculateG() {
		BigInteger h = BigInteger.valueOf(2); // common value for h
		do {
			g = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
			h = h.add(BigInteger.ONE);
		} while (g.equals(BigInteger.ONE));
	}

	/**
	 * Calculate the prime number p. p = k*q + 1
	 * 
	 * @param lBits the number of bits of the prime number
	 */
	private void calculatePQ(int lBits, int nBits) {

		generateStartValues(lBits, nBits);

		// make p a prime number
		while (!p.isProbablePrime(100)) {
			p = p.add(q);

			// if p is too large, restart the process
			if (p.bitLength() > lBits) {
				generateStartValues(lBits, nBits);
			}
		}
	}

	/**
	 * Generate the start values p and q.
	 * 
	 * @param lBits the number of bits for p.
	 * @param nBits the number of bits for q.
	 */
	private void generateStartValues(int lBits, int nBits) {
		// generate a random prime number q
		q = BigInteger.probablePrime(nBits, new SecureRandom());

		// expand p to the desired bit length
		p = q.shiftLeft(lBits - nBits).add(BigInteger.ONE);
	}

	/**
	 * Sign a message using the private key.
	 * 
	 * @param m the message to be signed
	 * @return an array of two BigIntegers representing the signature
	 */
	public BigInteger[] sign(char[] m) {

		BigInteger k;
		do {
			k = new BigInteger(q.bitLength(), new SecureRandom());
		} while (k.equals(BigInteger.ZERO));

		BigInteger r;
		do {
			r = g.modPow(k, p).mod(q);
		} while (r.equals(BigInteger.ZERO));

		BigInteger s;
		do {
			s = k.modInverse(q).multiply(new BigInteger(hash.hash(new String(m).getBytes())).add(x.multiply(r))).mod(q);
		} while (s.equals(BigInteger.ZERO));

		return new BigInteger[] { r, s };
	}

	/**
	 * Verify the signature of a message using the public key.
	 * 
	 * @param signature the signature to be verified
	 * @param message   the message to be verified
	 * @return true if the signature is valid, false otherwise
	 * @throws IllegalArgumentException if the signature is invalid
	 */
	public boolean verify(BigInteger[] signature, char[] message) {
		if (signature.length != 2) {
			throw new IllegalArgumentException("Invalid signature");
		}
		BigInteger r = signature[0];
		BigInteger s = signature[1];

		if (r.compareTo(BigInteger.ONE) < 0 || r.compareTo(q) >= 0) {
			return false;
		}

		if (s.compareTo(BigInteger.ONE) < 0 || s.compareTo(q) >= 0) {
			return false;
		}

		BigInteger w = s.modInverse(q);
		BigInteger u1 = new BigInteger(hash.hash(new String(message).getBytes())).multiply(w).mod(q);
		BigInteger u2 = r.multiply(w).mod(q);
		BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);

		return v.equals(r);
	}

}
