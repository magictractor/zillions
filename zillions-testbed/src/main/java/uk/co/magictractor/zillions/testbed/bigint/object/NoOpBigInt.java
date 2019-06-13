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
package uk.co.magictractor.zillions.testbed.bigint.object;

import uk.co.magictractor.zillions.api.BigInt;

/**
 * A useless implementation of BigInt. Always has a value of zero, and all
 * operations have no effect. Just used by some unit tests.
 */
public class NoOpBigInt implements BigInt {

    @Override
    public NoOpBigInt copy() {
        return new NoOpBigInt();
    }

    @Override
    public BigInt add(BigInt y) {
        return this;
    }

    @Override
    public BigInt add(int y) {
        return this;
    }

    @Override
    public BigInt subtract(BigInt y) {
        return this;
    }

    @Override
    public BigInt subtract(int y) {
        return this;
    }

    @Override
    public BigInt multiply(BigInt y) {
        return this;
    }

    @Override
    public BigInt multiply(int y) {
        return this;
    }

    @Override
    public BigInt pow(int exponent) {
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
        return (other instanceof NoOpBigInt);
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
