package crypt_basics.rsa;

import java.math.BigInteger;

public class RSAKeygen {

	private BigInteger n;
	private BigInteger phiN;
	private BigInteger e;
	private BigInteger d;

	public RSAKeygen(BigInteger p, BigInteger q, BigInteger e) {
		n = p.multiply(q);
		phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		this.e = e;
	}

	public BigInteger generatePrivateKey() {
		this.d = e.modInverse(phiN);
		return d;
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getE() {
		return e;
	}

	public BigInteger getD() {
		return d;
	}
}
