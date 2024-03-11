package crypt_basics.ec;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import crypt_basics.ec.EC.ECPoint;

class ECTest {

	@Test
	void testAddition1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("10"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(33), result.getX());
		assertEquals(BigInteger.valueOf(63), result.getY());
	}

	@Test
	void testAddition2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("17"), new BigInteger("10"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(1), result.getX());
		assertEquals(BigInteger.valueOf(54), result.getY());
	}

	@Test
	void testAddition_P1Inf() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint();
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(95), result.getX());
		assertEquals(BigInteger.valueOf(31), result.getY());
	}

	@Test
	void testAddition_P2Inf() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));
		ECPoint point2 = ec.new ECPoint();

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(95), result.getX());
		assertEquals(BigInteger.valueOf(31), result.getY());
	}

	@Test
	void testAddition_P1EqualP2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(74), result.getX());
		assertEquals(BigInteger.valueOf(77), result.getY());
	}

	@Test
	void testMultiplikation_n1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.ONE, point1);

		assertEquals(BigInteger.valueOf(95), result.getX());
		assertEquals(BigInteger.valueOf(31), result.getY());
	}

	@Test
	void testMultiplikation_n2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.TWO, point1);

		assertEquals(BigInteger.valueOf(74), result.getX());
		assertEquals(BigInteger.valueOf(77), result.getY());
	}

	@Test
	void testMultiplikation_n10() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.TEN, point1);

		assertEquals(BigInteger.valueOf(80), result.getX());
		assertEquals(BigInteger.valueOf(87), result.getY());
	}
}
