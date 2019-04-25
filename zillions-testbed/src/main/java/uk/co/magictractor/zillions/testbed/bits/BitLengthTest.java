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
package uk.co.magictractor.zillions.testbed.bits;

import static uk.co.magictractor.zillions.core.environment.Environment.getBestAvailableImplementation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.api.bits.BigIntBitLength;

public class BitLengthTest {

    //    public BitLengthTest() {
    //        super(BigInt::bitLength);
    //    }

    @Test
    public void testZero() {
        check(0, 0);
    }

    @Test
    public void testMinimumPositive() {
        check(1, 1);
    }

    @Test
    public void testMinusOne() {
        check(-1, 0);
    }

    @Test
    public void testMinimumNegative() {
        check(-2, 1);
    }

    @Test
    public void testPositive() {
        check(240, 8);
    }

    @Test
    public void testBelowBoundaryPositive() {
        check(255, 8);
    }

    @Test
    public void testAboveBoundaryPositive() {
        check(256, 9);
    }

    @Test
    public void testNegative() {
        check(-240, 8);
    }

    @Test
    public void testAboveBoundaryNegative() {
        check(-256, 8);
    }

    @Test
    public void testBelowBoundaryNegative() {
        check(-257, 9);
    }

    private void check(long value, int expected) {
        check(BigIntFactory.from(value), expected);
    }

    private void check(BigInt value, int expected) {
        int actual = getBestAvailableImplementation(BigIntBitLength.class).bitLength(value);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}
