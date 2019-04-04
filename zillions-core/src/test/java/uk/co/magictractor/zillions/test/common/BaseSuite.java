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

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import uk.co.magictractor.math.zillions.test.bits.BitSuite;
import uk.co.magictractor.zillions.test.arithmetic.ArithmeticSuite;
import uk.co.magictractor.zillions.test.object.ObjectSuite;

@RunWith(ReuseSuite.class)
@SuiteClasses({ObjectSuite.class, ArithmeticSuite.class, BitSuite.class})
public abstract class BaseSuite
{
  // This class is left intentionally blank.
}
