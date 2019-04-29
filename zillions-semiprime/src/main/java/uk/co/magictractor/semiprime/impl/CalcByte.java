package uk.co.magictractor.semiprime.impl;

import uk.co.magictractor.zillions.core.BigInt;

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

}
