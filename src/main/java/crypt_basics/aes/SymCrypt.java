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

public class SymCrypt {

	private Cipher cypher;
	private SecretKey key;

	/**
	 * AES constructor
	 * 
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public SymCrypt(String password, String algorithm)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		cypher = Cipher.getInstance(algorithm);
		
		byte[] salt = SecureRandom.getSeed(cypher.getBlockSize());

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, cypher.getBlockSize() * 8);
		key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}

	/**
	 * Creates an IV.
	 * 
	 * @return Random IV.
	 */
	private IvParameterSpec createIv() {
		SecureRandom random = new SecureRandom();
		byte[] ivBytes = new byte[cypher.getBlockSize()];
		random.nextBytes(ivBytes);
		return new IvParameterSpec(ivBytes);
	}

	/**
	 * Encrypt message
	 * 
	 * @param msg
	 * @return
	 * 
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 */
	public byte[] encrypt(String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {
		IvParameterSpec iv = createIv();

		cypher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cypherText = cypher.doFinal(msg.getBytes());
		byte[] ivBytes = iv.getIV();

		byte[] concat = new byte[ivBytes.length + cypherText.length];

		System.arraycopy(ivBytes, 0, concat, 0, ivBytes.length);
		System.arraycopy(cypherText, 0, concat, ivBytes.length, cypherText.length);

		return concat;
	}

	/**
	 * Decrypt message
	 * 
	 * @param stream
	 * @return
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 */
	public String decrypt(byte[] stream) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {

		byte[] iv = new byte[cypher.getBlockSize()];
		byte[] cypherText = new byte[stream.length - iv.length];

		System.arraycopy(stream, 0, iv, 0, iv.length);
		System.arraycopy(stream, iv.length, cypherText, 0, cypherText.length);

		cypher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		return new String(cypher.doFinal(cypherText));
	}
}
