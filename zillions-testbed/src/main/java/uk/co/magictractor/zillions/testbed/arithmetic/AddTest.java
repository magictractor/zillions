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
package uk.co.magictractor.zillions.testbed.arithmetic;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.magictractor.zillions.core.BigIntFactory.from;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;

public class AddTest
{
  @Test
  public void testAddSmallPositiveNumbers() {
    BigInt bigInt1 = from("10");
    BigInt bigInt2 = from("4");
    bigInt1.add(bigInt2);
    BigInt expected = from("14");
    assertThat(bigInt1).isEqualTo(expected);
  }

}
