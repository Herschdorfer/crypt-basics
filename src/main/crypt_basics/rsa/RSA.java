package crypt_basics.rsa;

import java.math.BigInteger;

public class RSA {
	private RSAKeygen keygen;

	public RSA(BigInteger p, BigInteger q, BigInteger e) {
		this.keygen = new RSAKeygen(p, q, e);
		this.keygen.generatePrivateKey();
	}

	public RSA(int bits, BigInteger e) {
		this.keygen = new RSAKeygen(BigInteger.probablePrime(bits, new java.util.Random()),
				BigInteger.probablePrime(bits, new java.util.Random()), e);
		this.keygen.generatePrivateKey();
	}

	public RSA(int bits) {
		this.keygen = new RSAKeygen(BigInteger.probablePrime(bits, new java.util.Random()),
				BigInteger.probablePrime(bits, new java.util.Random()), BigInteger.valueOf(65537));
		this.keygen.generatePrivateKey();
	}

	public BigInteger[] encrypt(char[] m) {
		BigInteger[] result = new BigInteger[m.length];
		for (int i = 0; i < m.length; i++) {
			result[i] = BigInteger.valueOf(m[i]).modPow(keygen.getE(), keygen.getN());
		}
		return result;
	}

	public char[] decrypt(BigInteger[] c) {
		char[] result = new char[c.length];
		for (int i = 0; i < c.length; i++) {
			result[i] = (char) (c[i].modPow(keygen.getD(), keygen.getN()).intValue());
		}
		return result;
	}
}
