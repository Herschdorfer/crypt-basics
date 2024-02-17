package crypt_basics.rsa;

import java.math.BigInteger;

import crypt_basics.Algorithm;

public class RSA implements Algorithm {
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

	@Override
	public char[] encrypt(char[] m) {
		for (int i = 0; i < m.length; i++) {
			m[i] = (char) (BigInteger.valueOf(m[i]).modPow(keygen.getE(), keygen.getN()).intValue());
		}
		return m;
	}

	@Override
	public char[] decrypt(char[] c) {
        for (int i = 0; i < c.length; i++) {
            c[i] = (char) (BigInteger.valueOf(c[i]).modPow(keygen.getD(), keygen.getN()).intValue());
        }
        return c;		
	}
}
