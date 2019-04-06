package uk.co.magictractor.zillions.core;

public class NumptyBigInt implements BigInt {

	/**
	 * Utter numpty! Just uses a long so only works for small numbers. Doesn't
	 * matter, the "big number" tests don't hit this. Tests in z-core probably won't
	 * exercise this class, just create instances.
	 */
	private long n;

	public NumptyBigInt(long n) {
		this.n = n;
	}

	@Override
	public BigInt add(BigInt y) {
		n += ((NumptyBigInt) y).n;
		return this;
	}

	@Override
	public BigInt add(long y) {
		n += y;
		return this;
	}

	@Override
	public BigInt subtract(BigInt y) {
		n -= ((NumptyBigInt) y).n;
		return this;
	}

	@Override
	public BigInt subtract(long y) {
		n -= y;
		return this;
	}

	@Override
	public BigInt multiply(BigInt y) {
		n *= ((NumptyBigInt) y).n;
		return this;
	}

	@Override
	public BigInt multiply(long y) {
		n *= y;
		return this;
	}

	@Override
	public BigInt and(BigInt y) {
		n &= ((NumptyBigInt) y).n;
		return this;
	}

	@Override
	public BigInt or(BigInt y) {
		n |= ((NumptyBigInt) y).n;
		return this;
	}

	@Override
	public BigInt xor(BigInt y) {
		n ^= ((NumptyBigInt) y).n;
		return this;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof NumptyBigInt) && ((NumptyBigInt) other).n == n;
	}

	@Override
	public int hashCode() {
		return (int) (n ^ (n >>> 32));
	}

	public String toString() {
		return Long.toString(n);
	}

}
