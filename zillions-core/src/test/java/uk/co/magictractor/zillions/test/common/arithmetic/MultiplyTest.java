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
package uk.co.magictractor.zillions.test.common.arithmetic;

import static uk.co.magictractor.zillions.core.BigIntCreate.from;

import org.junit.Assert;
import org.junit.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.test.common.ReuseTest;

public class MultiplyTest extends ReuseTest
{

  @Test
  public void testMultiplySmallPositiveNumbers() {
    BigInt bigInt1 = from("10");
    BigInt bigInt2 = from("4");
    bigInt1.multiply(bigInt2);
    BigInt expected = from("40");
    Assert.assertEquals(expected, bigInt1);
  }

}
