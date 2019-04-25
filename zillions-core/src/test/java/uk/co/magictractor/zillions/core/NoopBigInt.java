package uk.co.magictractor.zillions.core;

/**
 * An even more useless implementation of BigInt than NumptyBigInt. Always has a
 * value of zero, and all operations have no effect.
 *
 * Just used by unit tests for checking the type of BigInt created.
 */
public class NoopBigInt implements BigInt {

    @Override
    public NoopBigInt copy() {
        return new NoopBigInt();
    }

    @Override
    public BigInt add(BigInt y) {
        return this;
    }

    @Override
    public BigInt add(long y) {
        return this;
    }

    @Override
    public BigInt subtract(BigInt y) {
        return this;
    }

    @Override
    public BigInt subtract(long y) {
        return this;
    }

    @Override
    public BigInt multiply(BigInt y) {
        return this;
    }

    @Override
    public BigInt multiply(long y) {
        return this;
    }

    @Override
    public BigInt and(BigInt y) {
        return this;
    }

    @Override
    public BigInt or(BigInt y) {
        return this;
    }

    @Override
    public BigInt xor(BigInt y) {
        return this;
    }

    @Override
    public BigInt not() {
        return this;
    }

    @Override
    public BigInt negate() {
        return this;
    }

    @Override
    public BigInt abs() {
        return this;
    }

    @Override
    public int signum() {
        return 0;
    }

    @Override
    public BigInt shiftLeft(int n) {
        return this;
    }

    @Override
    public BigInt shiftRight(int n) {
        return this;
    }

    @Override
    public BigInt setBit(int n) {
        return this;
    }

    @Override
    public BigInt clearBit(int n) {
        return this;
    }

    @Override
    public BigInt flipBit(int n) {
        return this;
    }

    @Override
    public boolean testBit(int n) {
        return false;
    }

    @Override
    public int compareTo(BigInt o) {
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof NoopBigInt);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "0";
    }

}
