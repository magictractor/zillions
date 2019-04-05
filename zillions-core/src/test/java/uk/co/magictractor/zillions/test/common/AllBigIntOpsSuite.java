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
package uk.co.magictractor.zillions.test.common;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import uk.co.magictractor.zillions.test.common.arithmetic.AddTest;
import uk.co.magictractor.zillions.test.common.arithmetic.MultiplyTest;
import uk.co.magictractor.zillions.test.common.bits.XorTest;
import uk.co.magictractor.zillions.test.common.object.EqualsTest;
import uk.co.magictractor.zillions.test.common.object.HashCodeTest;
import uk.co.magictractor.zillions.test.common.object.ToStringTest;

// @SelectClasses does not work with nested suites

// TODO! remove JUnitPlatform with a later version of JUnit (5.5/5.6?)
// JUnitPlatform leans on JUnit4, JUnit5 suite support coming.
// See https://github.com/junit-team/junit5/issues/744.
@RunWith(JUnitPlatform.class)
//@RunWith(ReuseSuite.class)
//@SuiteClasses({ObjectSuite.class, ArithmeticSuite.class, BitSuite.class})
//@SelectClasses({ObjectSuite.class, ArithmeticSuite.class, BitSuite.class})
@SelectClasses({ ToStringTest.class, EqualsTest.class, HashCodeTest.class , AddTest.class, MultiplyTest.class, XorTest.class})
public abstract class AllBigIntOpsSuite
{
  // This class is left intentionally blank.
}

// Hmm. Maybe don't need a suite - just a tag?!'
