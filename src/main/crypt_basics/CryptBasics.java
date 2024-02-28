package crypt_basics;

import java.math.BigInteger;
import java.util.Arrays;

import crypt_basics.dsa.DSA;
import crypt_basics.rsa.RSA;

public class CryptBasics {

	public static void main(String[] args) {
		CLIParser parser = new CLIParser(args);

		switch (parser.getMode()) {
		case CRYPT:
			crypt(parser);
			break;
		case SIGN:
			sign(parser);
			break;
		default:
			System.out.println("Mode not found");
			return;
		}
	}

	private static void sign(CLIParser parser) {
		
		SignatureAlgorithm algorithm = null;
		
		switch (parser.getAlgorithm()) {
		case DSA:
			algorithm = new DSA(parser.getLBits(), parser.getNBits());
			break;
		default:
			System.out.println("Algorithm not found");
			System.exit(1);
		}
		
		BigInteger[] signature = algorithm.sign(parser.getInput());
		System.out.println("Signature: " + Arrays.toString(signature));
		System.out.println("Verification: " + algorithm.verify(signature, parser.getInput()));
		
	}

	private static void crypt(CLIParser parser) {
		Algorithm algorithm = null;

		switch (parser.getAlgorithm()) {
		case RSA:
			algorithm = new RSA(parser.getBits());
			break;
		default:
			System.out.println("Algorithm not found");
			return;
		}

		BigInteger[] c = algorithm.encrypt(parser.getInput());
		System.out.println("Encrypted message: " + Arrays.toString(c));

		char[] m = algorithm.decrypt(c);
		System.out.println("Decrypted message: " + Arrays.toString(m));

	}
}
