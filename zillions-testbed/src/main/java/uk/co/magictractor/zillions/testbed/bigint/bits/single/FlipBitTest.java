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
package uk.co.magictractor.zillions.testbed.bigint.bits.single;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.environment.BigIntFactory;
import uk.co.magictractor.zillions.testbed.bigint.OpTestIntParam;
import uk.co.magictractor.zillions.testbed.tags.WithinSuite;

@WithinSuite
public class FlipBitTest extends OpTestIntParam {

    public FlipBitTest() {
        super(BigInt::flipBit);
    }

    @Test
    public void testPositiveNumberFlipOn() {
        check(9, 2, 13);
    }

    @Test
    public void testPositiveNumberFlipOff() {
        check(9, 3, 1);
    }

    // -9 in binary is 111...1110111

    @Test
    public void testNegativeNumberFlipOn() {
        check(-9, 3, -1);
    }

    @Test
    public void testNegativeNumberFlipOff() {
        check(-9, 4, -25);
    }

    @Test
    public void testInvalidBitNum() {
        Assertions.assertThatThrownBy(() -> BigIntFactory.from(0).flipBit(-1))
                .isExactlyInstanceOf(ArithmeticException.class)
                .hasMessage("Negative bit address");
    }

}
