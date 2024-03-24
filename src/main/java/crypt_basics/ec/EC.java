package crypt_basics.ec;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
		 * 
		 * @param other The other point
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

		public ECPoint negate() {
			return new ECPoint(x, y.negate().mod(curveField));
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
	 * Calculates the NAF of a given number.
	 * 
	 * @note based on https://en.wikipedia.org/wiki/Non-adjacent_form
	 * 
	 * @return The order of a point
	 */
	private List<Integer> calculateNAF(int n) {
		List<Integer> z = new ArrayList<>();
		while (n > 0) {
			if (n % 2 == 1) {
				int zi = 2 - (n % 4);
				z.add(zi);
				n -= zi;
			} else {
				z.add(0);
			}
			n /= 2;

		}

		return z;
	}

	/**
	 * Elliptic curve scalar multiplication.
	 * 
	 * @param n     The scalar
	 * @param point The point
	 * @return The product of a scalar and a point
	 */
	public ECPoint scalarMultiplication(BigInteger n, ECPoint point) {

		BigInteger k;
		ECPoint pointQ;
		if (n.equals(BigInteger.ZERO)) {
			// 0 * point(x,y) = 0
			return new ECPoint();
		} else if (n.compareTo(BigInteger.ZERO) < 0) {
			// -n * point(x,y) = n * (-point(x,y))
			pointQ = point.negate();
			k = n.negate();
		} else {
			// n * point(x,y)
			pointQ = point;
			k = n;
		}

		ECPoint pointS = new ECPoint();

		List<Integer> z = calculateNAF(k.intValue());

		// addition-subtraction chain method
		for (int i = z.size() - 1; i >= 0; i--) {
			pointS = pointAddition(pointS, pointS);
			if (z.get(i) == 1) {
				pointS = pointAddition(pointS, pointQ);
			} else if (z.get(i) == -1) {
				pointS = pointSubtraction(pointS, pointQ);
			}
		}

		return pointS;

	}

	/**
	 * Elliptic curve point addition
	 * 
	 * @param p0 The first point
	 * @param p1 The second point
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

	/**
	 * Elliptic curve point subtraction
	 * 
	 * @param p0 The first point
	 * @param p1 The second point
	 * @return The subtraction of two points
	 */
	public ECPoint pointSubtraction(ECPoint p0, ECPoint p1) {
		return pointAddition(p0, p1.negate());
	}
}
