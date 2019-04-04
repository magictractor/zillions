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
package uk.co.magictractor.math.big.discovery;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.magictractor.math.big.create.CreateStrategy;
import uk.co.magictractor.math.big.gmp.jna.GmpJnaCreateStrategy;
import uk.co.magictractor.math.environment.CachedStrategies;
import uk.co.magictractor.math.junit.ClassAssert;

public class SpiDiscoveryStrategyTest
{

  private SpiDiscoveryStrategy _testee = new SpiDiscoveryStrategy();

  @Test
  public void testSuccessfulDiscovery() {
    CachedStrategies<CreateStrategy> discovered = _testee.discoverImplementations(CreateStrategy.class);

    assertEquals(1, discovered.allAvailable().size());
    ClassAssert.assertType(GmpJnaCreateStrategy.class, discovered.firstAvailable());
  }

  @Test
  public void testUnsuccessfulDiscovery() {
    CachedStrategies<Test> discovered = _testee.discoverImplementations(Test.class);

    assertEquals(0, discovered.allAvailable().size());
  }

}
