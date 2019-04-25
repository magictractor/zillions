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
package uk.co.magictractor.zillions.testbed.arithmetic;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.testbed.OpTestNoParam;

public class SignumTest extends OpTestNoParam<Integer> {

    public SignumTest() {
        super(Integer.class, BigInt::signum);
    }

    @Test
    public void testSignumSmallPositiveNumber() {
        check(10, 1);
    }

    @Test
    public void testSignumSmallNegativeNumber() {
        check(-10, -1);
    }

    @Test
    public void testSignumZero() {
        check(0, 0);
    }

    @Test
    public void testZeroAfterArithmetic() {
        BigInt x = BigIntFactory.from(7);
        x.subtract(x);
        check(x, 0);
    }

    @Test
    public void testNegativeAfterArithmetic() {
        BigInt x = BigIntFactory.from(7);
        BigInt y = BigIntFactory.from(99);
        x.subtract(y);
        check(x, -1);
    }

}
