package crypt_basics;

import java.math.BigInteger;

/**
 * Algorithm
 * 
 * Interface for encryption and decryption algorithms
 */
public interface Algorithm {

	/**
	 * Encrypts a message
	 * 
	 * @param m message
	 * @return encrypted message
	 */
	char[] encrypt(char m[]);

	/**
     * Decrypts a message
     * 
     * @param c encrypted message
     * @return decrypted message
     */
	char[] decrypt(char c[]);

}