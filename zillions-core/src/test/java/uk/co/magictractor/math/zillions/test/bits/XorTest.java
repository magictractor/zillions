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
package uk.co.magictractor.math.zillions.test.bits;

import static uk.co.magictractor.zillions.core.BigIntCreate.from;

import org.junit.Assert;
import org.junit.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.test.common.ReuseTest;

public class XorTest extends ReuseTest
{
  // private static final BigInt EXPECTED_POSITIVE = from("9");
  // private static final BigInt EXPECTED_NEGATIVE = from("-9");

  @Test
  public void testXorPositivePositive() {
    BigInt i = from("10");
    BigInt j = from("3");
    i.xor(j);
    BigInt expected = from("9");
    Assert.assertEquals(expected, i);
  }

  @Test
  public void testXorPositiveNegative() {
    BigInt i = from("10");
    BigInt j = from("-3");
    i.xor(j);
    BigInt expected = from("-9");
    Assert.assertEquals(expected, i);
  }

  @Test
  public void testXorNegativePositive() {
    BigInt i = from("-10");
    BigInt j = from("3");
    i.xor(j);
    BigInt expected = from("-11");
    Assert.assertEquals(expected, i);
  }

  @Test
  public void testXorNegativeNegative() {
    BigInt i = from("-10");
    BigInt j = from("-3");
    i.xor(j);
    BigInt expected = from("11");
    Assert.assertEquals(expected, i);
  }

}
