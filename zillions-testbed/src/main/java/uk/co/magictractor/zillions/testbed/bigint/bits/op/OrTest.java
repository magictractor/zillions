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

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.testbed.bigint.OpTestBigIntParam;
import uk.co.magictractor.zillions.testbed.tags.WithinSuite;

@WithinSuite
public class OrTest extends OpTestBigIntParam {

    public OrTest() {
        super(BigInt::or);
    }

    @Test
    public void testOrPositivePositive() {
        check(10, 3, 11);
    }

    @Test
    public void testOrPositiveNegative() {
        check(10, -3, -1);
    }

    @Test
    public void testOrNegativePositive() {
        check(-10, 3, -9);
    }

    @Test
    public void testOrNegativeNegative() {
        check(-10, -3, -1);
    }

}
