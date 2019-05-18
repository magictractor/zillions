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
package uk.co.magictractor.zillions.gmp;

import org.junit.jupiter.api.extension.RegisterExtension;

import uk.co.magictractor.zillions.core.junit.TestContextExtension;
import uk.co.magictractor.zillions.gmp.bits.GmpBigIntBitCount;
import uk.co.magictractor.zillions.gmp.bits.GmpBigIntBitLength;
import uk.co.magictractor.zillions.testbed.bigint.BigIntSuite;

public class GmpBigIntSuite extends BigIntSuite {

    @RegisterExtension
    public static TestContextExtension todo = new TestContextExtension(GmpCreateStrategy.class, GmpBigIntBitCount.class,
        GmpBigIntBitLength.class);

    // Nothing here. Full test suite is inherited from testbed parent suite.

}
