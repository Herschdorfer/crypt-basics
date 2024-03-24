package crypt_basics.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class represents a hash function. It provides a method for hashing data.
 */
public class Hash {

	private MessageDigest algorithm;

	/**
	 * Constructor
	 * 
	 * @param nBits the number of bits of the hash function
	 */
	public Hash(int nBits) throws NoSuchAlgorithmException {
		switch (nBits) {
			case 128:
				algorithm = MessageDigest.getInstance("MD5");
				break;
			case 160:
				algorithm = MessageDigest.getInstance("SHA-1");
				break;
			case 256:
				algorithm = MessageDigest.getInstance("SHA-256");
				break;
			case 384:
				algorithm = MessageDigest.getInstance("SHA-384");
				break;
			case 512:
				algorithm = MessageDigest.getInstance("SHA-512");
				break;
			default:
				throw new IllegalArgumentException("Invalid number of bits for the hash function");
		}
	}

	/**
	 * Hash the data
	 * 
	 * @param data the data to be hashed
	 * @return the hash value
	 */
	public byte[] hash(byte[] data) {
		return algorithm.digest(data);
	}
}
