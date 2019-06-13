package uk.co.magictractor.semiprime.impl;

import com.google.common.base.MoreObjects;

import uk.co.magictractor.zillions.api.BigInt;

public class CalcByte {

    // 0-255
    private final int _leftByte;
    // 0-255
    private final int _rightByte;
    private final BigInt _carried;

    public CalcByte(int leftByte, int rightByte, BigInt carried) {
        this._leftByte = leftByte;
        this._rightByte = rightByte;
        this._carried = carried;
    }

    public int left() {
        return _leftByte;
    }

    public int right() {
        return _rightByte;
    }

    public BigInt carried() {
        return _carried;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("left", _leftByte).add("right", _rightByte).add("carried",
            _carried).toString();
    }

}
