package crypt_basics;

import crypt_basics.rsa.RSA;

public class CryptBasics {

	public static void main(String[] args) {
		CLIParser parser = new CLIParser(args);
		
		Algorithm algorithm = null;
		
		switch (parser.getAlgorithm()) {
		case RSA:
			algorithm = new RSA(parser.getBits());
			
			break;
		}
		
		// algorithm.encrypt(parser.getInput());
		System.out.println("Encrypted message: " + parser.getInput());
		
	}

}
