/**
 * Copyright 2015-2019 Ken Dobson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.magictractor.zillions.biginteger;

import java.math.BigInteger;

import uk.co.magictractor.zillions.api.BigInt;

public class BigIntegerBigInt implements BigInt {

    private BigInteger _bigInteger;

    public BigIntegerBigInt(String decimal) {
        _bigInteger = new BigInteger(decimal);
    }

    public BigIntegerBigInt(long value) {
        _bigInteger = BigInteger.valueOf(value);
    }

    public BigIntegerBigInt(BigInteger bigInteger) {
        _bigInteger = bigInteger;
    }

    @Override
    public BigIntegerBigInt copy() {
        return new BigIntegerBigInt(_bigInteger);
    }

    @Override
    public BigInt add(BigInt y) {
        _bigInteger = _bigInteger.add(((BigIntegerBigInt) y)._bigInteger);
        return this;
    }

    @Override
    public BigInt add(int y) {
        _bigInteger = _bigInteger.add(BigInteger.valueOf(y));
        return this;
    }

    @Override
    public BigInt subtract(BigInt y) {
        _bigInteger = _bigInteger.subtract(((BigIntegerBigInt) y)._bigInteger);
        return this;
    }

    @Override
    public BigInt subtract(int y) {
        _bigInteger = _bigInteger.subtract(BigInteger.valueOf(y));
        return this;
    }

    @Override
    public BigInt multiply(BigInt y) {
        _bigInteger = _bigInteger.multiply(((BigIntegerBigInt) y)._bigInteger);
        return this;
    }

    @Override
    public BigInt multiply(int y) {
        _bigInteger = _bigInteger.multiply(BigInteger.valueOf(y));
        return this;
    }

    @Override
    public BigInt pow(int exponent) {
        _bigInteger = _bigInteger.pow(exponent);
        return this;
    }

    @Override
    public BigInt negate() {
        _bigInteger = _bigInteger.negate();
        return this;
    }

    @Override
    public BigInt abs() {
        _bigInteger = _bigInteger.abs();
        return this;
    }

    @Override
    public int signum() {
        return _bigInteger.signum();
    }

    @Override
    public BigInt and(BigInt y) {
        _bigInteger = _bigInteger.and(((BigIntegerBigInt) y)._bigInteger);
        return this;
    }

    @Override
    public BigInt or(BigInt y) {
        _bigInteger = _bigInteger.or(((BigIntegerBigInt) y)._bigInteger);
        return this;
    }

    @Override
    public BigInt xor(BigInt y) {
        _bigInteger = _bigInteger.xor(((BigIntegerBigInt) y)._bigInteger);
        return this;
    }

    @Override
    public BigInt not() {
        _bigInteger = _bigInteger.not();
        return this;
    }

    @Override
    public BigInt shiftLeft(int n) {
        _bigInteger = _bigInteger.shiftLeft(n);
        return this;
    }

    @Override
    public BigInt shiftRight(int n) {
        _bigInteger = _bigInteger.shiftRight(n);
        return this;
    }

    @Override
    public BigInt setBit(int n) {
        _bigInteger = _bigInteger.setBit(n);
        return this;
    }

    @Override
    public BigInt clearBit(int n) {
        _bigInteger = _bigInteger.clearBit(n);
        return this;
    }

    @Override
    public BigInt flipBit(int n) {
        _bigInteger = _bigInteger.flipBit(n);
        return this;
    }

    @Override
    public boolean testBit(int n) {
        return _bigInteger.testBit(n);
    }

    public BigInteger getInternalValue() {
        return _bigInteger;
    }

    public void setInternalValue(BigInteger bigInteger) {
        _bigInteger = bigInteger;
    }

    @Override
    public int compareTo(BigInt other) {
        return _bigInteger.compareTo(((BigIntegerBigInt) other)._bigInteger);
    }

    @Override
    public boolean equals(Object other) {
        boolean equals = false;
        if (other != null && other.getClass() == BigIntegerBigInt.class) {
            equals = _bigInteger.equals(((BigIntegerBigInt) other)._bigInteger);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return _bigInteger.hashCode();
    }

    @Override
    public String toString() {
        return _bigInteger.toString();
    }

    // TODO! use this only and remove non-static getInternalValue() method?
    public static BigInteger getInternalValue(BigInt x) {
        return ((BigIntegerBigInt) x)._bigInteger;
    }

}
