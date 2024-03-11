package crypt_basics.ec;

import java.math.BigInteger;
import java.util.Objects;

public class EC {

	public class ECPoint {
		private BigInteger x;
		private BigInteger y;

		/**
		 * A point at infinity
		 */
		public ECPoint() {
			this.x = BigInteger.ZERO;
			this.y = BigInteger.ZERO;
		}

		/**
		 * A point on the elliptic curve
		 * 
		 * @param x
		 * @param y
		 */
		public ECPoint(BigInteger x, BigInteger y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Check if two points are equal
		 */
		public boolean equals(Object other) {

			if (!(other instanceof ECPoint)) {
				return false;
			}

			return ((this.getX().subtract(((ECPoint) other).getX()).mod(field).compareTo(BigInteger.ZERO) == 0)
					&& (this.getY().subtract(((ECPoint) other).getY()).mod(field).compareTo(BigInteger.ZERO) == 0));
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
	}

	private BigInteger field;
	private BigInteger curveCoeffA;

	public EC(BigInteger field, BigInteger curveCoeffA) {
		this.field = field;
		this.curveCoeffA = curveCoeffA;
	}

	/**
	 * Elliptic curve scalar multiplication
	 * 
	 * @return
	 */
	public ECPoint scalarMultiplication(BigInteger k, ECPoint point) {

		ECPoint result = new ECPoint();

		int i;
		int length = k.bitLength();

		if ((k.compareTo(BigInteger.ZERO) != 0) && (!point.equals(new ECPoint()))) {
			for (i = 0; i < length; i++) {
				if (k.testBit(i)) {
					result = pointAddition(result, point);
				}
				point = pointAddition(point, point);
			}
		}

		return result;

	}

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
			// calc = (3 * p1.x^2 + this.curveCoeffA) * (2 * p1.y)^-1 mod this.field
			calc = (three.multiply(p1.getX().pow(2)).add(this.curveCoeffA))
					.multiply((p1.getY().multiply(BigInteger.TWO)).modInverse(this.field));
		} else {
			// Normal case
			// calc = (p2.y - p1.y) * (p2.x - p1.x)^-1 mod this.field
			calc = p2.getY().subtract(p1.getY()).multiply(p2.getX().subtract(p1.getX()).modInverse(this.field));
		}

		// result.x = calc^2 - p1.x - p2.x mod this.field
		result.x = calc.pow(2).subtract(p1.getX()).subtract(p2.getX()).mod(this.field);
		// result.y = calc * (p1.x - result.x) - p1.y mod this.field
		result.y = calc.multiply(p1.getX().subtract(result.getX())).subtract(p1.getY()).mod(this.field);

		return result;
	}
}
