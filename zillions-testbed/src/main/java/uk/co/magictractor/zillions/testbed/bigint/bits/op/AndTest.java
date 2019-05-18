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
package uk.co.magictractor.zillions.testbed.bigint.bits.op;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.testbed.bigint.OpTestBigIntParam;

public class AndTest extends OpTestBigIntParam {

    public AndTest() {
        super(BigInt::and);
    }

    @Test
    public void testAndPositivePositive() {
        check(10, 3, 2);
    }

    @Test
    public void testAndPositiveNegative() {
        check(10, -3, 8);
    }

    @Test
    public void testAndNegativePositive() {
        check(-10, 3, 2);
    }

    @Test
    public void testAndNegativeNegative() {
        check(-10, -3, -12);
    }

}
