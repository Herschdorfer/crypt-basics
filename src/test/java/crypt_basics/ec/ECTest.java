package crypt_basics.ec;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import crypt_basics.ec.EC.ECPoint;

class ECTest {

	@Test
	void testAddition1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("10"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(33), result.getX());
		assertEquals(BigInteger.valueOf(63), result.getY());
	}
	
	@Test
	void testAddition2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("17"), new BigInteger("10"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(1), result.getX());
		assertEquals(BigInteger.valueOf(54), result.getY());
	}
}
