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
		BigInteger calc = BigInteger.ZERO;
		BigInteger three = new BigInteger("3");
		boolean isNotDone = true;

		if (p1.equals(new ECPoint())) {
			result = p2;
			isNotDone = false;
		}

		if ((p2.equals(new ECPoint())) && (isNotDone)) {
			result = p1;
			isNotDone = false;
		}

		if (isNotDone) {
			if (p1.equals(p2)) {
				calc = three.multiply(p1.x.multiply(p1.x)).add(this.curveCoeffA)
						.multiply((p1.y.multiply(BigInteger.TWO)).modInverse(this.field));
			} else {
				calc = p2.y.subtract(p1.y).multiply(p2.x.subtract(p1.x).modInverse(this.field));
			}

			result.x = calc.multiply(calc).subtract(p1.x).subtract(p2.x).mod(this.field);
			result.y = calc.multiply(p1.x.subtract(result.x)).subtract(p1.y).mod(this.field);
		}

		return result;
	}
}
