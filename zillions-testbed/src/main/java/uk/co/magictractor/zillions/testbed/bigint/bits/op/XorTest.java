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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.testbed.bigint.OpTestBigIntParam;
import uk.co.magictractor.zillions.testbed.tags.WithinSuite;

@WithinSuite
public class XorTest extends OpTestBigIntParam {

    public XorTest() {
        super(BigInt::xor);
    }

    @Test
    public void testXorPositivePositive() {
        check(10, 3, 9);
    }

    @Test
    public void testXorPositiveNegative() {
        check(10, -3, -9);
    }

    @Test
    public void testXorNegativePositive() {
        check(-10, 3, -11);
    }

    @Test
    public void testXorNegativeNegative() {
        check(-10, -3, 11);
    }

    // For checking behaviour with dynamic test suite
    @Test
    @Disabled
    public void disabled() {
        Assertions.fail("@Disabled tests in the suite should not be executed");
    }

    // For checking behaviour with dynamic test suite
    //@Test
    //public void fails() {
    //    check(-10, -3, 999);
    //}

}
