package crypt_basics.hmac;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

/**
 * The HMAC class represents a Hash-based Message Authentication Code (HMAC)
 * algorithm. It provides methods for calculating the HMAC and verifying the
 * integrity of a message using HMAC.
 */
public class HMAC {

	private byte[] key;
	private Mac mac;

	/**
	 * Constructs a new HMAC object with the specified key.
	 * 
	 * @param key the secret key used for HMAC calculation
	 * @throws NoSuchAlgorithmException if the HmacSHA256 algorithm is not available
	 */
	public HMAC(byte[] key) throws NoSuchAlgorithmException {
		this.key = key;
		this.mac = Mac.getInstance("HmacSHA256");
	}

	/**
	 * Calculates the HMAC of the given message using the secret key.
	 * 
	 * @param message the message to calculate HMAC for
	 * @return the HMAC value as a byte array
	 * @throws InvalidKeyException if the secret key is invalid
	 */
	public byte[] calc(byte[] message) throws InvalidKeyException {
		// encrypt the message using the key
		mac.init(new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256"));
		return mac.doFinal(message);
	}

	/**
	 * Verifies the integrity of a message by comparing its HMAC with the given
	 * signature.
	 * 
	 * @param message the message to verify
	 * @param mac     the HMAC signature to compare against
	 * @return true if the HMAC signature matches, false otherwise
	 * @throws InvalidKeyException if the secret key is invalid
	 */
	public boolean verify(byte[] message, byte[] mac) throws InvalidKeyException {
		return MessageDigest.isEqual(mac, calc(message));
	}
}
