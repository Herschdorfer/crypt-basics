package crypt_basics;

import java.math.BigInteger;

public interface SignatureAlgorithm {
	public BigInteger[] sign(char[] m);

	public boolean verify(BigInteger[] s, char[] m);
}
