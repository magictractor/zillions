/**
 * Copyright 2015 Ken Dobson
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
package uk.co.magictractor.zillions.core;

/**
 * <p>
 * BigInt implementations are mutable. All methods should return "this" to allow
 * daisy chaining of operations.
 * </p>
 *
 * <p>
 * As well as the methods defined on this interface, implementations will
 * require implementations of toString(), equals() and hashCode().
 * </p>
 *
 * <p>
 * Note that for a different BigInt implementation with the same numeric value,
 * equals() should return false. All other methods which have a BigInt parameter
 * should throw a RuntimeException if passed a BigInt with different
 * implementation type. In most cases that will be a ClassCastException to avoid
 * the (very small) overhead of always checking the type of the parameter.
 * </p>
 */
public interface BigInt extends EnhancedComparable<BigInt> {

    BigInt add(BigInt y);

    BigInt add(long y);

    BigInt subtract(BigInt y);

    BigInt subtract(long y);

    BigInt multiply(BigInt y);

    BigInt multiply(long y);

    /** Negate value. */
    BigInt negate();

    /** Absolute value. */
    BigInt abs();

    int signum();

    // Bit operations.

    BigInt or(BigInt y);

    BigInt and(BigInt y);

    BigInt xor(BigInt y);

    // 1's complement
    BigInt not();

    // also andNot() in BigInteger

    /**
     * For these ops, bit 0 is the least significant bit. Big operations should act
     * on a 2's complement representation of the number.
     * https://en.wikipedia.org/wiki/Two%27s_complement
     */

    BigInt setBit(int n);

    BigInt clearBit(int n);

    BigInt flipBit(int n);

    boolean testBit(int n);

    BigInt shiftLeft(int n);

    BigInt shiftRight(int n);

    BigInt copy();
}
