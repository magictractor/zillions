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
package uk.co.magictractor.zillions.testbed.bigint.object;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.testbed.bigint.OpTestSingleParam;
import uk.co.magictractor.zillions.testbed.tags.WithinSuite;

@WithinSuite
public class CompareTest extends OpTestSingleParam<BigInt, Integer> {

    public CompareTest() {
        super(BigInt.class, Integer.class, BigInt::compareTo);
    }

    @Test
    public void testEquals() {
        check(0, 0, 0);
        check(-10, -10, 0);
    }

    @Test
    public void testLessThan() {
        check(0, 1, -1);
        check(-1, 0, -1);
    }

    @Test
    public void testGreaterThan() {
        check(1, 0, 1);
        check(0, -1, 1);
    }

}
