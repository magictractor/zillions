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
package uk.co.magictractor.zillions.testbed.bigint.arithmetic;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.jura.suite.WithinSuite;
import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.testbed.bigint.OpTestBigIntParam;

@WithinSuite
public class MultiplyTest extends OpTestBigIntParam {

    public MultiplyTest() {
        super(BigInt::multiply);
    }

    @Test
    public void testMultiplySmallPositiveNumbers() {
        check(10, 4, 40);
    }

    @Test
    public void testMultiplySmallNegativeNumbers() {
        check(-10, -4, 40);
    }

    @Test
    public void testMultiplySmallNegativeBySmallPositive() {
        check(-10, 4, -40);
    }

    @Test
    public void testMultiplySmallPositiveBySmallNegative() {
        check(10, -4, -40);
    }

}
