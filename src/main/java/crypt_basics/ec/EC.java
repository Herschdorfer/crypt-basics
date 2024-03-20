package crypt_basics.ec;

import java.math.BigInteger;
import java.util.Objects;

public class EC {

	public class ECPoint {
		private BigInteger x;
		private BigInteger y;

		/**
		 * A point at zero
		 */
		public ECPoint() {
			this.x = BigInteger.ZERO;
			this.y = BigInteger.ZERO;
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

			return ((this.getX().subtract(((ECPoint) other).getX()).mod(curveField).compareTo(BigInteger.ZERO) == 0)
					&& (this.getY().subtract(((ECPoint) other).getY()).mod(curveField)
							.compareTo(BigInteger.ZERO) == 0));
		}

		public int hashCode() {
			return Objects.hash(x, y);
		}

		public BigInteger getX() {
			return x;
		}

		public BigInteger getY() {
			return y;
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
	public ECPoint pointAddition(ECPoint p1, ECPoint p2) {

		ECPoint result = new ECPoint();
		BigInteger calc;
		BigInteger three = BigInteger.valueOf(3L);

		// 0 + p2 = p2
		if (p1.equals(new ECPoint())) {
			return p2;
		}

		// p1 + 0 = p1
		if (p2.equals(new ECPoint())) {
			return p1;
		}

		if (p1.equals(p2)) {
			// Special case for doubling
			// calc = (3 * p1.x^2 + A) * (2 * p1.y)^-1 mod field
			calc = (three.multiply(p1.getX().pow(2)).add(this.curveA))
					.multiply((p1.getY().multiply(BigInteger.TWO)).modInverse(this.curveField));
		} else {
			// Normal case
			// calc = (p2.y - p1.y) * (p2.x - p1.x)^-1 mod field
			calc = p2.getY().subtract(p1.getY()).multiply(p2.getX().subtract(p1.getX()).modInverse(this.curveField));
		}

		// result.x = calc^2 - p1.x - p2.x mod field
		result.x = calc.pow(2).subtract(p1.getX()).subtract(p2.getX()).mod(this.curveField);

		// result.y = calc * (p1.x - result.x) - p1.y mod field
		result.y = calc.multiply(p1.getX().subtract(result.getX())).subtract(p1.getY()).mod(this.curveField);

		return result;
	}
}
