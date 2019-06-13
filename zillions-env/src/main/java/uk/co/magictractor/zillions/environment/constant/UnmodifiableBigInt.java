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
package uk.co.magictractor.zillions.environment.constant;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.environment.BigIntFactory;

public class UnmodifiableBigInt implements BigInt {

    public static final UnmodifiableBigInt MINUS_ONE;
    public static final UnmodifiableBigInt ZERO;
    public static final UnmodifiableBigInt ONE;
    public static final UnmodifiableBigInt TWO;

    private static final int CACHE_MIN = -256;
    private static final int CACHE_MAX = 255;
    private static final UnmodifiableBigInt[] CACHE = new UnmodifiableBigInt[CACHE_MAX - CACHE_MIN + 1];

    // This static block keeps the public constant declarations before the private cache declaration.
    // Alternatively could use a Checkstyle suppression.
    static {
        MINUS_ONE = valueOf(-1);
        ZERO = valueOf(0);
        ONE = valueOf(1);
        TWO = valueOf(2);
    }

    private final BigInt _wrapped;

    public UnmodifiableBigInt(BigInt wrapped) {
        _wrapped = wrapped;
    }

    private UnsupportedOperationException boom() {
        return new UnsupportedOperationException("This BigInt may not be modified");
    }

    // TODO! what if other is also wrapped - compareTo and equals will have problems...
    @Override
    public int compareTo(BigInt other) {
        return _wrapped.compareTo(other);
    }

    @Override
    public BigInt add(BigInt y) {
        throw boom();
    }

    @Override
    public BigInt add(int y) {
        throw boom();
    }

    @Override
    public BigInt subtract(BigInt y) {
        throw boom();
    }

    @Override
    public BigInt subtract(int y) {
        throw boom();
    }

    @Override
    public BigInt multiply(BigInt y) {
        throw boom();
    }

    @Override
    public BigInt multiply(int y) {
        throw boom();
    }

    @Override
    public BigInt pow(int exponent) {
        throw boom();
    }

    @Override
    public BigInt negate() {
        throw boom();
    }

    @Override
    public BigInt abs() {
        return _wrapped.abs();
    }

    @Override
    public int signum() {
        return _wrapped.signum();
    }

    @Override
    public BigInt or(BigInt y) {
        throw boom();
    }

    @Override
    public BigInt and(BigInt y) {
        throw boom();
    }

    @Override
    public BigInt xor(BigInt y) {
        throw boom();
    }

    @Override
    public BigInt not() {
        throw boom();
    }

    @Override
    public BigInt setBit(int n) {
        throw boom();
    }

    @Override
    public BigInt clearBit(int n) {
        throw boom();
    }

    @Override
    public BigInt flipBit(int n) {
        throw boom();
    }

    @Override
    public boolean testBit(int n) {
        return _wrapped.testBit(n);
    }

    @Override
    public BigInt shiftLeft(int n) {
        throw boom();
    }

    @Override
    public BigInt shiftRight(int n) {
        throw boom();
    }

    @Override
    public BigInt copy() {
        return this;
    }

    public BigInt unwrap() {
        return _wrapped;
    }

    public static UnmodifiableBigInt valueOf(int i) {
        UnmodifiableBigInt result;
        if (i >= CACHE_MIN && i <= CACHE_MAX) {
            result = CACHE[i - CACHE_MIN];
            if (result == null) {
                result = new UnmodifiableBigInt(BigIntFactory.from(i));
                CACHE[i - CACHE_MIN] = result;
            }
        }
        else {
            // Value out of range for cache.
            result = new UnmodifiableBigInt(BigIntFactory.from(i));
        }

        return result;
    }

    private static UnmodifiableBigInt getFromCache(int i) {
        return CACHE[i - CACHE_MIN];
    }

}
