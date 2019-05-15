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

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.testbed.bigint.OpTestIntParam;

public class SetBitTest extends OpTestIntParam {

    public SetBitTest() {
        super(BigInt::setBit);
    }

    @Test
    public void testPositiveNumberChangedBit() {
        check(9, 2, 13);
    }

    @Test
    public void testPositiveNumberUnchangedBit() {
        check(9, 3, 9);
    }

    // -9 in binary is 111...1110111

    @Test
    public void testNegativeNumberChangedBit() {
        check(-9, 3, -1);
    }

    @Test
    public void testNegativeNumberUnchangedBit() {
        check(-9, 2, -9);
    }

    @Test
    public void testInvalidBitNum() {
        Assertions.assertThatThrownBy(() -> BigIntFactory.from(0).setBit(-1)).isExactlyInstanceOf(
            ArithmeticException.class).hasMessage("Negative bit address");
    }

}