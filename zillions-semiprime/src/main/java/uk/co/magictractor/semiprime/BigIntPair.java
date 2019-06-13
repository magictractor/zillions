package uk.co.magictractor.semiprime;

import uk.co.magictractor.zillions.api.BigInt;

public class BigIntPair {

    private final BigInt _left;
    private final BigInt _right;

    public static final BigIntPair ordered(BigInt x, BigInt y) {
        if (x.compareTo(y) <= 0) {
            return new BigIntPair(x, y);
        }
        else {
            return new BigIntPair(y, x);
        }
    }

    private BigIntPair(BigInt left, BigInt right) {
        this._left = left;
        this._right = right;
    }

    public BigInt getLeft() {
        return _left;
    }

    public BigInt getRight() {
        return _right;
    }

}
