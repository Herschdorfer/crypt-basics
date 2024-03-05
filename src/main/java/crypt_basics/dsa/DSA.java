package crypt_basics.dsa;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Logger;

/**
 * This class represents the Digital Signature Algorithm (DSA). It provides
 * methods for generating keys, signing messages, and verifying signatures.
 */
public class DSA {

	/** The Logger */
	Logger logger = Logger.getLogger(DSA.class.getName());

	// prvate key
	private BigInteger x;

	// public key
	private BigInteger y;

	// parameters
	private BigInteger q;
	private BigInteger p;
	private BigInteger g;

	private MessageDigest hash;

	/**
	 * Constructor
	 * 
	 * @param lBits the number of bits of the prime number
	 * @param nBits the number of bits of the hash function
	 */
	public DSA(int lBits, int nBits) {
		try {
			this.hash = getHash(nBits);
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
	 * 
	 * @param nBits the number of bits of the hash function
	 */
	private MessageDigest getHash(int nBits) throws NoSuchAlgorithmException {
		switch (nBits) {
		case 256:
			return MessageDigest.getInstance("SHA-256");
		case 384:
			return MessageDigest.getInstance("SHA-384");
		case 512:
			return MessageDigest.getInstance("SHA-512");
		default:
			throw new IllegalArgumentException("Invalid number of bits for the hash function");
		}
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

		// generate a random prime number q
		this.q = BigInteger.probablePrime(nBits, new SecureRandom());

		// expand p to the desired bit length
		p = q.shiftLeft(lBits - nBits).add(BigInteger.ONE);

		// make p a prime number
		while (!p.isProbablePrime(10)) {
			p = p.add(q);
			if (p.bitLength() > lBits) {
				p = p.subtract(BigInteger.ONE.shiftLeft(lBits - nBits));
			}
		}
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
			s = k.modInverse(q).multiply(new BigInteger(hash.digest(new String(m).getBytes())).add(x.multiply(r)))
					.mod(q);
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
		BigInteger u1 = new BigInteger(hash.digest(new String(message).getBytes())).multiply(w).mod(q);
		BigInteger u2 = r.multiply(w).mod(q);
		BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);

		return v.equals(r);
	}

}
