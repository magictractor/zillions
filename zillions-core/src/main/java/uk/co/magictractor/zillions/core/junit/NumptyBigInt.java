
package uk.co.magictractor.zillions.core.junit;

import uk.co.magictractor.zillions.core.BigInt;

/**
 * A very simple implementation of BigInt which only has correct behaviour for
 * relatively small numbers, because it is implemented with operations on a
 * long.
 * 
 * The "big number" tests don't hit this. Tests in zillions-core probably won't
 * exercise this class, just create instances. Tests in zillions-testbed may use
 * this to test the behaviour of BigInt.equals() and BigInt.compare() with other
 * types of BigInt implementations.
 */
public class NumptyBigInt implements BigInt {

    private long _x;

    public NumptyBigInt(long n) {
        _x = n;
    }

    public NumptyBigInt copy() {
        return new NumptyBigInt(_x);
    }

    @Override
    public BigInt add(BigInt y) {
        _x += ((NumptyBigInt) y)._x;
        return this;
    }

    @Override
    public BigInt add(long y) {
        _x += y;
        return this;
    }

    @Override
    public BigInt subtract(BigInt y) {
        _x -= ((NumptyBigInt) y)._x;
        return this;
    }

    @Override
    public BigInt subtract(long y) {
        _x -= y;
        return this;
    }

    @Override
    public BigInt multiply(BigInt y) {
        _x *= ((NumptyBigInt) y)._x;
        return this;
    }

    @Override
    public BigInt multiply(long y) {
        _x *= y;
        return this;
    }

    @Override
    public BigInt and(BigInt y) {
        _x &= ((NumptyBigInt) y)._x;
        return this;
    }

    @Override
    public BigInt or(BigInt y) {
        _x |= ((NumptyBigInt) y)._x;
        return this;
    }

    @Override
    public BigInt xor(BigInt y) {
        _x ^= ((NumptyBigInt) y)._x;
        return this;
    }

    @Override
    public BigInt not() {
        _x = ~_x;
        return this;
    }

    @Override
    public BigInt negate() {
        _x = -_x;
        return this;
    }

    @Override
    public BigInt abs() {
        if (_x < 0) {
            _x = -_x;
        }
        return this;
    }

    @Override
    public int signum() {
        if (_x == 0) {
            return 0;
        }
        else if (_x > 0) {
            return 1;
        }
        else {
            return -1;
        }
    }

    @Override
    public BigInt shiftLeft(int n) {
        _x = _x << n;
        return this;
    }

    @Override
    public BigInt shiftRight(int n) {
        _x = _x >> n;
        return this;
    }

    // TODO! implement these - or (more likely) bin this class
    @Override
    public BigInt setBit(int n) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public BigInt clearBit(int n) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public BigInt flipBit(int n) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public boolean testBit(int n) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int compareTo(BigInt other) {
        long y = ((NumptyBigInt) other)._x;
        if (_x == y) {
            return 0;
        }
        else if (_x > y) {
            return 1;
        }
        else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof NumptyBigInt) && ((NumptyBigInt) other)._x == _x;
    }

    @Override
    public int hashCode() {
        return (int) (_x ^ (_x >>> 32));
    }

    public String toString() {
        return Long.toString(_x);
    }

}
