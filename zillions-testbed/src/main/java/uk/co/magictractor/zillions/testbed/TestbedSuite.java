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
package uk.co.magictractor.zillions.testbed;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.suite.api.SelectClasses;

import uk.co.magictractor.zillions.testbed.arithmetic.AbsTest;
import uk.co.magictractor.zillions.testbed.arithmetic.AddTest;
import uk.co.magictractor.zillions.testbed.arithmetic.MultiplyTest;
import uk.co.magictractor.zillions.testbed.arithmetic.NegateTest;
import uk.co.magictractor.zillions.testbed.arithmetic.SubtractTest;
import uk.co.magictractor.zillions.testbed.bits.AndTest;
import uk.co.magictractor.zillions.testbed.bits.ClearBitTest;
import uk.co.magictractor.zillions.testbed.bits.FlipBitTest;
import uk.co.magictractor.zillions.testbed.bits.OrTest;
import uk.co.magictractor.zillions.testbed.bits.SetBitTest;
import uk.co.magictractor.zillions.testbed.bits.TestBitTest;
import uk.co.magictractor.zillions.testbed.bits.XorTest;
import uk.co.magictractor.zillions.testbed.bits.shift.ShiftLeftTest;
import uk.co.magictractor.zillions.testbed.bits.shift.ShiftRightTest;
import uk.co.magictractor.zillions.testbed.object.CompareTest;
import uk.co.magictractor.zillions.testbed.object.EqualsTest;
import uk.co.magictractor.zillions.testbed.object.HashCodeTest;
import uk.co.magictractor.zillions.testbed.object.ToStringTest;
import uk.co.magictractor.zillions.testbed.random.RandomTest;

// JUnit5 suite support coming.
// See https://github.com/junit-team/junit5/issues/744.

// TODO! revert to suite of suites
//@SelectClasses({ObjectSuite.class, ArithmeticSuite.class, BitSuite.class})

@SelectClasses({ ToStringTest.class, EqualsTest.class, CompareTest.class, HashCodeTest.class, AddTest.class,
		SubtractTest.class, MultiplyTest.class, NegateTest.class, AbsTest.class, AndTest.class, OrTest.class,
		XorTest.class, ShiftLeftTest.class, ShiftRightTest.class, 
		SetBitTest.class, ClearBitTest.class, FlipBitTest.class, TestBitTest.class, 
		RandomTest.class })
public abstract class TestbedSuite {

	@TestFactory
	public Stream<DynamicNode> suiteFactory() {
		return new DynamicSuite(this).stream();
	}

	@Test
	public void fail() {
		Assertions.fail();
	}

}
