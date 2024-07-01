package crypt_basics.ec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import crypt_basics.ec.EC.ECPoint;

class ECTest {

	/**
	 * Test addition of two points
	 */
	@Test
	void testAddition1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("10"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(33), result.x);
		assertEquals(BigInteger.valueOf(63), result.y);
	}

	/**
	 * Test addition of two points
	 */
	@Test
	void testAddition2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("17"), new BigInteger("10"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(1), result.x);
		assertEquals(BigInteger.valueOf(54), result.y);
	}

	/**
	 * Test addition of two points, one is the point at infinity
	 */
	@Test
	void testAddition_P1Inf() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint();
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(95), result.x);
		assertEquals(BigInteger.valueOf(31), result.y);
	}

	/**
	 * Test addition of two points, one is the point at infinity
	 */
	@Test
	void testAddition_P2Inf() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));
		ECPoint point2 = ec.new ECPoint();

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(95), result.x);
		assertEquals(BigInteger.valueOf(31), result.y);
	}

	/**
	 * Test addition of two points, both are equal
	 */
	@Test
	void testAddition_P1EqualP2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(74), result.x);
		assertEquals(BigInteger.valueOf(77), result.y);
	}

	private static Stream<Arguments> scalarMultiplicationTestCases() {
		return Stream.of(
				// scalar, expectedX, expectedY
				Arguments.of(BigInteger.valueOf(1), BigInteger.valueOf(95), BigInteger.valueOf(31)),
				Arguments.of(BigInteger.valueOf(2), BigInteger.valueOf(74), BigInteger.valueOf(77)),
				Arguments.of(BigInteger.valueOf(3), BigInteger.valueOf(21), BigInteger.valueOf(24)),
				Arguments.of(BigInteger.valueOf(4), BigInteger.valueOf(24), BigInteger.valueOf(95)),
				Arguments.of(BigInteger.valueOf(5), BigInteger.valueOf(3), BigInteger.valueOf(91)),
				Arguments.of(BigInteger.valueOf(6), BigInteger.valueOf(46), BigInteger.valueOf(72)),
				Arguments.of(BigInteger.valueOf(7), BigInteger.valueOf(84), BigInteger.valueOf(37)),
				Arguments.of(BigInteger.valueOf(8), BigInteger.valueOf(65), BigInteger.valueOf(32)),
				Arguments.of(BigInteger.valueOf(9), BigInteger.valueOf(52), BigInteger.valueOf(29)),
				Arguments.of(BigInteger.valueOf(10), BigInteger.valueOf(80), BigInteger.valueOf(87)));
	}

	/**
	 * Test scalar multiplication with different scalars
	 */
	@ParameterizedTest
	@MethodSource("scalarMultiplicationTestCases")
	void testScalarMultiplication(BigInteger scalar, BigInteger expectedX, BigInteger expectedY) {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(scalar, point1);

		Assertions.assertEquals(expectedX, result.x);
		Assertions.assertEquals(expectedY, result.y);
	}

	/**
	 * Test scalar multiplication with negative scalar
	 */
	@Test
	void testMultiplikation_kMinus() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.valueOf(-2), point1);

		assertEquals(BigInteger.valueOf(74), result.x);
		assertEquals(BigInteger.valueOf(20), result.y);
	}

	/**
	 * Test scalar multiplication with scalar 0
	 */
	@Test
	void testMultiplikation_k0() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.ZERO, point1);

		assertEquals(BigInteger.ZERO, result.x);
		assertEquals(BigInteger.ZERO, result.y);
	}

	/**
	 * Test scalar multiplication with point at infinity
	 */
	@Test
	void testMultiplikation_P0() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint();

		ECPoint result = ec.scalarMultiplication(BigInteger.TEN, point1);

		assertEquals(BigInteger.ZERO, result.x);
		assertEquals(BigInteger.ZERO, result.y);
	}

	/**
	 * Test point not on curve
	 *
	 */
	@Test
	void testPointNotOnCurve_1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		BigInteger x = new BigInteger("96");
		BigInteger y = new BigInteger("31");

		assertThrows(IllegalArgumentException.class, () -> ec.new ECPoint(x, y));
	}

	/**
	 * Test point not on curve
	 *
	 */
	@Test
	void testPointNotOnCurve_2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		BigInteger x = new BigInteger("95");
		BigInteger y = new BigInteger("32");

		assertThrows(IllegalArgumentException.class, () -> ec.new ECPoint(x, y));
	}

	/**
	 * Test of scalar multiplication with the curve secp256k1
	 *
	 */
	@ParameterizedTest
	@MethodSource("testData")
	void testMultiplikation_secP256k1_GM(String expectedX, String expectedY, BigInteger scalar) {
		EC ec = new EC(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16),
				new BigInteger("0"), new BigInteger("7"));
		ECPoint point1 = ec.new ECPoint(
				new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
				new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16));

		ECPoint result = ec.scalarMultiplication(scalar, point1);

		ECPoint expected = ec.new ECPoint(new BigInteger(expectedX, 16), new BigInteger(expectedY, 16));

		assertEquals(expected.x, result.x);
		assertEquals(expected.y, result.y);
	}

	private static Stream<Arguments> testData() {
		return Stream.of(
				Arguments.of(
						"79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798",
						"483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8",
						BigInteger.ONE),
				Arguments.of(
						"C6047F9441ED7D6D3045406E95C07CD85C778E4B8CEF3CA7ABAC09B95C709EE5",
						"1AE168FEA63DC339A3C58419466CEAEEF7F632653266D0E1236431A950CFE52A",
						new BigInteger("2")),
				Arguments.of(
						"F9308A019258C31049344F85F89D5229B531C845836F99B08601F113BCE036F9",
						"388F7B0F632DE8140FE337E62A37F3566500A99934C2231B6CB9FD7584B8E672",
						new BigInteger("3")),
				Arguments.of(
						"E493DBF1C10D80F3581E4904930B1404CC6C13900EE0758474FA94ABE8C4CD13",
						"51ED993EA0D455B75642E2098EA51448D967AE33BFBDFE40CFE97BDC47739922",
						new BigInteger("4")),
				Arguments.of(
						"2F8BDE4D1A07209355B4A7250A5C5128E88B84BDDC619AB7CBA8D569B240EFE4",
						"D8AC222636E5E3D6D4DBA9DDA6C9C426F788271BAB0D6840DCA87D3AA6AC62D6",
						new BigInteger("5")),
				Arguments.of(
						"FFF97BD5755EEEA420453A14355235D382F6472F8568A18B2F057A1460297556",
						"AE12777AACFBB620F3BE96017F45C560DE80F0F6518FE4A03C870C36B075F297",
						new BigInteger("6")));
	}

	/**
	 * Test of calculations with the curve secp256k1
	 *
	 * G1 + G2
	 */
	@Test
	void testAddition_secP256k1_G1G2() {
		EC ec = new EC(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16),
				new BigInteger("0"), new BigInteger("7"));
		ECPoint point1 = ec.new ECPoint(
				new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
				new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16));
		ECPoint point2 = ec.new ECPoint(
				new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
				new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(new BigInteger(
				"C6047F9441ED7D6D3045406E95C07CD85C778E4B8CEF3CA7ABAC09B95C709EE5", 16),
				result.x);
		assertEquals(new BigInteger(
				"1AE168FEA63DC339A3C58419466CEAEEF7F632653266D0E1236431A950CFE52A", 16),
				result.y);
	}

	/**
	 * Test of calculations with the curve secp256k1
	 *
	 * G1 + 2* G2
	 */
	@Test
	void testAddition_secP256k1_G1M2G2() {
		EC ec = new EC(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16),
				new BigInteger("0"), new BigInteger("7"));
		ECPoint point1 = ec.new ECPoint(
				new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
				new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16));
		ECPoint point2 = ec.new ECPoint(
				new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
				new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16));

		ECPoint result = ec.pointAddition(point1, ec.scalarMultiplication(BigInteger.TWO, point2));

		assertEquals(new BigInteger("F9308A019258C31049344F85F89D5229B531C845836F99B08601F113BCE036F9", 16),
				result.x);
		assertEquals(new BigInteger("388F7B0F632DE8140FE337E62A37F3566500A99934C2231B6CB9FD7584B8E672", 16),
				result.y);

	}

	/**
	 * Helper method to access the private method calculateNAF
	 *
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private Method getCalculateNAFMethod() throws NoSuchMethodException, SecurityException {
		Method method;

		method = EC.class.getDeclaredMethod("calculateNAF", BigInteger.class);
		method.setAccessible(true);
		return method;
	}

	/**
	 * Test of calculateNAF of the number 7
	 *
	 */
	@Test
	void testCalculateNAF_7() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		assertEquals(List.of(-1, 0, 0, 1), getCalculateNAFMethod().invoke(ec, BigInteger.valueOf(7)));
	}

	/**
	 * Test of calculateNAF of the number 13
	 *
	 */
	@Test
	void testCalculateNAF_13() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		assertEquals(List.of(1, 0, -1, 0, 1), getCalculateNAFMethod().invoke(ec, BigInteger.valueOf(13)));
	}

	/**
	 * Test of calculateNAF of the number 29
	 *
	 */
	@Test
	void testCalculateNAF_29() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		assertEquals(List.of(1, 0, -1, 0, 0, 1), getCalculateNAFMethod().invoke(ec, BigInteger.valueOf(29)));
	}

	/**
	 * Test of calculateNAF of the number 30
	 *
	 */
	@Test
	void testCalculateNAF_30() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		assertEquals(List.of(0, -1, 0, 0, 0, 1), getCalculateNAFMethod().invoke(ec, BigInteger.valueOf(30)));
	}

	/**
	 * Test of the toString method of the ECPoint class
	 *
	 */
	@Test
	void testECPointToString() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		assertEquals("x: 95, x: 31", point.toString());
	}

	/**
	 * Test equals method of the ECPoint class
	 *
	 * x1 = x2
	 * y1 = y2
	 */
	@Test
	void testECPointEquals_y() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		assertEquals(point1, point2);
	}

	/**
	 * Test equals method of the ECPoint class
	 *
	 * x1 = x2
	 * y1 != y2
	 */
	@Test
	void testECPointNotEquals_n1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("3"), new BigInteger("91"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("3"), new BigInteger("6"));

		Assertions.assertNotEquals(point1, point2);
	}

	/**
	 * Test equals method of the ECPoint class
	 *
	 * x1 != x2
	 * y1 = y2
	 */
	@Test
	void testECPointNotEquals_n2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("3"), new BigInteger("91"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("39"), new BigInteger("91"));

		Assertions.assertNotEquals(point1, point2);
	}

	/**
	 * Test equals method of the ECPoint class
	 *
	 * other != instance of ECPoint
	 */
	@Test
	void testECPointNotEquals4() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		Assertions.assertNotEquals(point1, new Object());
	}
}
