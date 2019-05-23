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
package uk.co.magictractor.zillions.testbed.bigint.bits.shift;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.testbed.bigint.OpTestIntParam;
import uk.co.magictractor.zillions.testbed.tags.WithinSuite;

@WithinSuite
public class ShiftLeftTest extends OpTestIntParam {

    public ShiftLeftTest() {
        super(BigInt::shiftLeft);
    }

    @Test
    public void testPositiveNumberPositiveShift() {
        check(13, 2, 52);
    }

    @Test
    public void testNegativeNumberPositiveShift() {
        check(-13, 2, -52);
    }

    @Test
    public void testPositiveNumberNegativeShift() {
        check(13, -2, 3);
    }

    @Test
    public void testNegativeNumberNegativeShift() {
        check(-13, -2, -4);
    }

}
