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
package uk.co.magictractor.zillions.testbed.bits;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.suite.api.SelectClasses;

import uk.co.magictractor.zillions.testbed.DynamicSuite;
import uk.co.magictractor.zillions.testbed.bits.shift.BitShiftSuite;

@SelectClasses({ AndTest.class, OrTest.class, XorTest.class, NotTest.class, BitShiftSuite.class, SetBitTest.class,
        ClearBitTest.class, FlipBitTest.class, TestBitTest.class, BitLengthTest.class, BitCountTest.class })
public class BitSuite {

    @TestFactory
    public Stream<DynamicNode> suiteFactory() {
        return new DynamicSuite(this).stream();
    }

}
