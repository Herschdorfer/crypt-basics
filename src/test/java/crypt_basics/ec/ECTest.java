package crypt_basics.ec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import crypt_basics.ec.EC.ECPoint;

class ECTest {

	@Test
	void testAddition1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("10"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("23"), new BigInteger("9"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(33), result.x);
		assertEquals(BigInteger.valueOf(63), result.y);
	}

	@Test
	void testAddition2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("17"), new BigInteger("10"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(1), result.x);
		assertEquals(BigInteger.valueOf(54), result.y);
	}

	@Test
	void testAddition_P1Inf() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint();
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(95), result.x);
		assertEquals(BigInteger.valueOf(31), result.y);
	}

	@Test
	void testAddition_P2Inf() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));
		ECPoint point2 = ec.new ECPoint();

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(95), result.x);
		assertEquals(BigInteger.valueOf(31), result.y);
	}

	@Test
	void testAddition_P1EqualP2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));
		ECPoint point2 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.pointAddition(point1, point2);

		assertEquals(BigInteger.valueOf(74), result.x);
		assertEquals(BigInteger.valueOf(77), result.y);
	}

	@Test
	void testMultiplikation_n1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.ONE, point1);

		assertEquals(BigInteger.valueOf(95), result.x);
		assertEquals(BigInteger.valueOf(31), result.y);
	}

	@Test
	void testMultiplikation_n2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.TWO, point1);

		assertEquals(BigInteger.valueOf(74), result.x);
		assertEquals(BigInteger.valueOf(77), result.y);
	}

	@Test
	void testMultiplikation_n3() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.valueOf(3), point1);

		assertEquals(BigInteger.valueOf(21), result.x);
		assertEquals(BigInteger.valueOf(24), result.y);
	}

	@Test
	void testMultiplikation_n5() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.valueOf(5), point1);

		assertEquals(BigInteger.valueOf(3), result.x);
		assertEquals(BigInteger.valueOf(91), result.y);
	}

	@Test
	void testMultiplikation_n6() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.valueOf(6), point1);

		assertEquals(BigInteger.valueOf(46), result.x);
		assertEquals(BigInteger.valueOf(72), result.y);
	}

	@Test
	void testMultiplikation_n7() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("3"), new BigInteger("6"));

		ECPoint result = ec.scalarMultiplication(BigInteger.valueOf(7), point1);

		assertEquals(BigInteger.valueOf(80), result.x);
		assertEquals(BigInteger.valueOf(10), result.y);
	}

	@Test
	void testMultiplikation_n10() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.TEN, point1);

		assertEquals(BigInteger.valueOf(80), result.x);
		assertEquals(BigInteger.valueOf(87), result.y);
	}

	@Test
	void testMultiplikation_k0() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint(new BigInteger("95"), new BigInteger("31"));

		ECPoint result = ec.scalarMultiplication(BigInteger.ZERO, point1);

		assertEquals(BigInteger.ZERO, result.x);
		assertEquals(BigInteger.ZERO, result.y);
	}

	@Test
	void testMultiplikation_P0() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));
		ECPoint point1 = ec.new ECPoint();

		ECPoint result = ec.scalarMultiplication(BigInteger.TEN, point1);

		assertEquals(BigInteger.ZERO, result.x);
		assertEquals(BigInteger.ZERO, result.y);
	}

	@Test
	void testPointNotOnCurve_1() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		BigInteger x = new BigInteger("96");
		BigInteger y = new BigInteger("31");

		assertThrows(IllegalArgumentException.class, () -> ec.new ECPoint(x, y));
	}

	@Test
	void testPointNotOnCurve_2() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		BigInteger x = new BigInteger("95");
		BigInteger y = new BigInteger("32");

		assertThrows(IllegalArgumentException.class, () -> ec.new ECPoint(x, y));
	}

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

	@Test
	void testCalculateNAF_7() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		List<Integer> naf = ec.calculateNAF(7);

		assertEquals(List.of(-1, 0, 0, 1), naf);

	}

	@Test
	void testCalculateNAF_13() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		List<Integer> naf = ec.calculateNAF(13);

		assertEquals(List.of(1, 0, -1, 0, 1), naf);

	}

	@Test
	void testCalculateNAF_29() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		List<Integer> naf = ec.calculateNAF(29);

		assertEquals(List.of(1, 0, -1, 0, 0, 1), naf);

	}

	@Test
	void testCalculateNAF_30() {
		EC ec = new EC(new BigInteger("97"), new BigInteger("2"), new BigInteger("3"));

		List<Integer> naf = ec.calculateNAF(30);

		assertEquals(List.of(0, -1, 0, 0, 0, 1), naf);
	}
}
