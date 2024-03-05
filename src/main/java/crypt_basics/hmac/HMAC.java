package crypt_basics.hmac;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

public class HMAC {

	private byte[] key;
	private Mac mac;

	public HMAC(byte[] key) throws NoSuchAlgorithmException {
		this.key = key;
		this.mac = Mac.getInstance("HmacSHA256");
	}

	public byte[] calc(byte[] message) throws InvalidKeyException {
		// encrypt the message using the key
		mac.init(new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256"));
		return mac.doFinal(message);
	}
	
	public boolean verify(byte[] message, byte[] signature) throws InvalidKeyException {
		return MessageDigest.isEqual(signature, calc(message));
	}
}
