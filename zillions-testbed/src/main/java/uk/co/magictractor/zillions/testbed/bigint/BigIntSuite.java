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
package uk.co.magictractor.zillions.testbed.bigint;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.suite.api.SelectClasses;

import uk.co.magictractor.zillions.testbed.bigint.arithmetic.AbsTest;
import uk.co.magictractor.zillions.testbed.bigint.arithmetic.AddTest;
import uk.co.magictractor.zillions.testbed.bigint.arithmetic.MultiplyTest;
import uk.co.magictractor.zillions.testbed.bigint.arithmetic.NegateTest;
import uk.co.magictractor.zillions.testbed.bigint.arithmetic.SubtractTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.AndTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.ClearBitTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.FlipBitTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.OrTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.SetBitTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.TestBitTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.XorTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.shift.ShiftLeftTest;
import uk.co.magictractor.zillions.testbed.bigint.bits.shift.ShiftRightTest;
import uk.co.magictractor.zillions.testbed.bigint.object.CompareTest;
import uk.co.magictractor.zillions.testbed.bigint.object.EqualsTest;
import uk.co.magictractor.zillions.testbed.bigint.object.HashCodeTest;
import uk.co.magictractor.zillions.testbed.bigint.object.ToStringTest;

// JUnit5 suite support coming.
// See https://github.com/junit-team/junit5/issues/744.

// TODO! revert to suite of suites
//@SelectClasses({ObjectSuite.class, ArithmeticSuite.class, BitSuite.class})

@SelectClasses({ ToStringTest.class, EqualsTest.class, CompareTest.class, HashCodeTest.class, AddTest.class,
        SubtractTest.class, MultiplyTest.class, NegateTest.class, AbsTest.class, AndTest.class, OrTest.class,
        XorTest.class, ShiftLeftTest.class, ShiftRightTest.class, SetBitTest.class, ClearBitTest.class,
        FlipBitTest.class, TestBitTest.class })
public abstract class BigIntSuite {

    @TestFactory
    public Stream<DynamicNode> suiteFactory() {
        return new DynamicSuite(this).stream();
    }

}
