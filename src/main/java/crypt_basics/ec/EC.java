package crypt_basics.ec;

import java.math.BigInteger;
import java.util.Objects;

public class EC {

	public class ECPoint {
		public final BigInteger x;
		public final BigInteger y;

		/**
		 * A point at zero
		 */
		public ECPoint() {
			x = BigInteger.ZERO;
			y = BigInteger.ZERO;
		}

		/**
		 * A point on the elliptic curve
		 * 
		 * @param x The x coordinate
		 * @param y The y coordinate
		 */
		public ECPoint(BigInteger x, BigInteger y) {
			this.x = x;
			this.y = y;

			if (!isOnCurve()) {
				throw new IllegalArgumentException("Point is not on the curve");
			}
		}

		/**
		 * Check if two points are equal
		 */
		public boolean equals(Object other) {

			if (!(other instanceof ECPoint)) {
				return false;
			}

			ECPoint otherPoint = (ECPoint) other;

			return x.subtract(otherPoint.x).mod(curveField).equals(BigInteger.ZERO)
					&& y.subtract(otherPoint.y).mod(curveField).equals(BigInteger.ZERO);
		}

		public int hashCode() {
			return Objects.hash(x, y);
		}

		public String toString() {
			return String.format("x: %s, x: %s", x.toString(), y.toString());
		}

		/**
		 * Check if the point is on the curve
		 */
		public boolean isOnCurve() {
			return (y.pow(2).subtract(x.pow(3).add(curveA.multiply(x)).add(curveB)).mod(curveField)
					.compareTo(BigInteger.ZERO) == 0);
		}

		public ECPoint minus() {
			return new ECPoint(x, y.negate());
		}
	}

	private BigInteger curveField;
	private BigInteger curveA;
	private BigInteger curveB;

	/**
	 * An minimal representation of a elliptic curve.
	 * 
	 * @param curveField The field of the elliptic curve
	 * @param curveA     The A parameter of the elliptic curve
	 * @param curveB     The B parameter of the elliptic curve
	 */
	public EC(BigInteger curveField, BigInteger curveA, BigInteger curveB) {
		this.curveField = curveField;
		this.curveA = curveA;
		this.curveB = curveB;
	}

	/**
	 * Elliptic curve scalar multiplication
	 * 
	 * @return The product of a scalar and a point
	 */
	public ECPoint scalarMultiplication(BigInteger n, ECPoint point) {

		// 0 * point(x,y) = 0
		if (n.equals(BigInteger.ZERO)) {
			return new ECPoint();
		}

		BigInteger k;
		ECPoint pointQ;
		if (n.compareTo(BigInteger.ZERO) < 0) {
			pointQ = point.minus();
			k = n.negate();
		} else {
			pointQ = point;
			k = n;
		}

		ECPoint pointS = pointQ;

		for (int i = 1; i < k.bitLength(); i++) {
			pointS = pointAddition(pointS, pointS);
			if ((k.multiply(BigInteger.valueOf(3))).testBit(i) && !k.testBit(i)) {
				pointS = pointAddition(pointS, pointQ);
			}

			if (!(k.multiply(BigInteger.valueOf(3))).testBit(i) && k.testBit(i)) {
				pointS = pointAddition(pointS, pointQ.minus());
			}
		}

		return pointS;

	}

	/**
	 * Elliptic curve point addition
	 * 
	 * @return The sum of two points
	 */
	public ECPoint pointAddition(ECPoint p0, ECPoint p1) {

		BigInteger lambda;

		// 0 + p1 = p1
		if (p0.equals(new ECPoint())) {
			return p1;
		}

		// p0 + 0 = p0
		if (p1.equals(new ECPoint())) {
			return p0;
		}

		if (!p0.equals(p1)) {
			// Normal case
			// lambda = (p1.y - p0.y) * (p1.x - p0.x)^-1 mod field
			lambda = p1.y.subtract(p0.y).multiply(p1.x.subtract(p0.x).modInverse(this.curveField));
		} else {
			// Special case for doubling
			// lambda = (3 * p0.x^2 + A) * (2 * p0.y)^-1 mod field
			lambda = (BigInteger.valueOf(3).multiply(p0.x.pow(2)).add(this.curveA))
					.multiply((p0.y.multiply(BigInteger.TWO)).modInverse(this.curveField));

		}

		// result.x = lambda^2 - p0.x - p1.x mod field
		BigInteger x = lambda.pow(2).subtract(p0.x).subtract(p1.x).mod(this.curveField);

		// result.y = lambda * (p0.x - result.x) - p0.y mod field
		BigInteger y = lambda.multiply(p0.x.subtract(x)).subtract(p0.y).mod(this.curveField);

		return new ECPoint(x, y);
	}
}
