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
package uk.co.magictractor.zillions.testbed.bits;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.testbed.OpTestNoParam;

public class NotTest extends OpTestNoParam<BigInt> {

    public NotTest() {
        super(BigInt.class, BigInt::not);
    }

    @Test
    public void testNotPositive() {
        check(10, -11);
    }

    @Test
    public void testNotNegative() {
        check(-10, 9);
    }

    @Test
    public void testNotLargePositive() {
        // TODO! truncated?
        // check(772866488294L, -772866488294L);
        check("772866488294", "-772866488295");
    }

    @Test
    public void testNotLargeNegative() {
        check("-772866488294", "772866488293");
    }

    @Test
    public void testNotZero() {
        check(0, -1);
    }

    @Test
    public void testNotMimimumPositive() {
        check(1, -2);
    }

    @Test
    public void testNotMinimumNegative() {
        check(-1, 0);
    }

}
