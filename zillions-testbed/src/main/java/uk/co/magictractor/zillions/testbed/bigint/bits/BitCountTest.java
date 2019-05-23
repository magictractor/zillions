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
package uk.co.magictractor.zillions.testbed.bigint.bits;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.bits.BigIntBitCount;
import uk.co.magictractor.zillions.environment.BigIntFactory;
import uk.co.magictractor.zillions.environment.Environment;

public class BitCountTest /* extends OpTestNoParam */ {

    //  public BitCountTest() {
    //      super(BigInt::bitCount);
    //  }

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
        check(0x99, 4);
    }

    @Test
    public void testNegative() {
        check(-0x99, 3);
    }

    private void check(long value, int expected) {
        check(BigIntFactory.from(value), expected);
    }

    private void check(BigInt value, int expected) {
        int actual = Environment.findImplementation(BigIntBitCount.class).bitCount(value);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}
