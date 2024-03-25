package crypt_basics.aes;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides symmetric encryption and decryption using the AES
 * algorithm.
 */
public class SymCrypt {

	private Cipher cipher;
	private SecretKey key;
	private SecureRandom random;

	/**
	 * Constructs a SymCrypt object with the specified password and algorithm.
	 * 
	 * @param password  the password used for encryption and decryption
	 * @param algorithm the encryption algorithm to be used
	 * @throws NoSuchAlgorithmException if the specified algorithm is not available
	 * @throws NoSuchPaddingException   if the specified padding scheme is not
	 *                                  available
	 * @throws InvalidKeySpecException  if the provided key specification is invalid
	 */
	public SymCrypt(String password, String algorithm)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		cipher = Cipher.getInstance(algorithm);
		random = new SecureRandom();

		byte[] salt = new byte[cipher.getBlockSize()];
		random.nextBytes(salt);

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, cipher.getBlockSize() * 8);
		key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

	}

	/**
	 * Creates an initialization vector (IV) for encryption.
	 * 
	 * @return a randomly generated IV
	 */
	private IvParameterSpec createIv() {
		byte[] ivBytes = new byte[cipher.getBlockSize()];
		random.nextBytes(ivBytes);
		return new IvParameterSpec(ivBytes);
	}

	/**
	 * Encrypts the given message using the initialized key and IV.
	 * 
	 * @param msg the message to be encrypted
	 * @return the encrypted message as a byte array
	 * @throws InvalidKeyException                if the provided key is invalid
	 * @throws BadPaddingException                if the padding is invalid
	 * @throws IllegalBlockSizeException          if the block size is invalid
	 * @throws InvalidAlgorithmParameterException if the provided algorithm
	 *                                            parameters are invalid
	 */
	public byte[] encrypt(String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {
		IvParameterSpec iv = createIv();

		if (cipher.getAlgorithm().contains("ECB")) {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(msg.getBytes());
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] cipherText = cipher.doFinal(msg.getBytes());
			byte[] ivBytes = iv.getIV();

			byte[] concat = new byte[ivBytes.length + cipherText.length];

			System.arraycopy(ivBytes, 0, concat, 0, ivBytes.length);
			System.arraycopy(cipherText, 0, concat, ivBytes.length, cipherText.length);
			return concat;
		}
	}

	/**
	 * Decrypts the given byte array using the initialized key and IV.
	 * 
	 * @param stream the byte array to be decrypted
	 * @return the decrypted message as a String
	 * @throws InvalidKeyException                if the provided key is invalid
	 * @throws BadPaddingException                if the padding is invalid
	 * @throws IllegalBlockSizeException          if the block size is invalid
	 * @throws InvalidAlgorithmParameterException if the provided algorithm
	 *                                            parameters are invalid
	 */
	public String decrypt(byte[] stream) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {

		if (cipher.getAlgorithm().contains("ECB")) {
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(stream));
		} else {
			byte[] iv = new byte[cipher.getBlockSize()];
			byte[] cipherText = new byte[stream.length - iv.length];

			System.arraycopy(stream, 0, iv, 0, iv.length);
			System.arraycopy(stream, iv.length, cipherText, 0, cipherText.length);

			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			return new String(cipher.doFinal(cipherText));
		}
	}
}
