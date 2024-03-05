package crypt_basics.aes;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 256);
		key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}

	/**
	 * Encrypt message
	 * 
	 * @param msg
	 * @param iv
	 * @return
	 * 
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 */
	public byte[] encrypt(String msg, IvParameterSpec iv) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		cypher.init(Cipher.ENCRYPT_MODE, key, iv);
		return cypher.doFinal(msg.getBytes());
	}

	/**
	 * Decrypt message
	 * 
	 * @param cypherText
	 * @param iv
	 * @return
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 */
	public String decrypt(byte[] cypherText, IvParameterSpec iv) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		cypher.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(cypher.doFinal(cypherText));
	}
}
