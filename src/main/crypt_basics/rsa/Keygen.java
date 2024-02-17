package crypt_basics.rsa;

import java.math.BigInteger;

public class Keygen {
	
	private BigInteger n;
	
	public Keygen(BigInteger p, BigInteger q) {
		n = p.multiply(q);
	}

	void generate_private_key() {
		
	}
}
