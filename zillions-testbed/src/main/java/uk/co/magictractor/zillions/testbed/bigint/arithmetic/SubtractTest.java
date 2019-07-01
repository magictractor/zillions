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

import uk.co.magictractor.jura.WithinSuite;
import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.testbed.bigint.OpTestBigIntParam;

@WithinSuite
public class SubtractTest extends OpTestBigIntParam {

    public SubtractTest() {
        super(BigInt::subtract);
    }

    @Test
    public void testSubtractSmallPosPosPos() {
        check(10, 4, 6);
    }

    @Test
    public void testSubtractSmallPosPosNeg() {
        check(4, 10, -6);
    }

    // PosNegNeg not possible
    @Test
    public void testSubtractSmallPosNegPos() {
        check(4, -10, 14);
    }

    @Test
    public void testSubtractSmallNegNegPos() {
        check(-4, -10, 6);
    }

    @Test
    public void testSubtractSmallNegNegNeg() {
        check(-10, -4, -6);
    }

    //   NegPosPos not possible
    @Test
    public void testSubtractSmallNegPosNeg() {
        check(-10, 4, -14);
    }

}
