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
package uk.co.magictractor.zillions.core.discovery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.NumptyCreateStrategy;
import uk.co.magictractor.zillions.core.create.CreateStrategy;
import uk.co.magictractor.zillions.core.environment.CachedStrategies;

public class SpiDiscoveryStrategyTest {

    private SpiDiscoveryStrategy _testee = new SpiDiscoveryStrategy();

    // NEXT - NPE in bootstrap.
    @Test
    @Disabled("Failing - @Disabled while setting up Maven builds")
    public void testSuccessfulDiscovery() {
        CachedStrategies<CreateStrategy> discovered = _testee.discoverImplementations(CreateStrategy.class);

        // assertEquals(1, discovered.allAvailable().size());
        assertThat(discovered.allAvailable()).hasSize(1);
        assertThat(discovered.bestAvailable()).isExactlyInstanceOf(NumptyCreateStrategy.class);
        // ClassAssert.assertType(GmpJnaCreateStrategy.class,
        // discovered.firstAvailable());
    }

    @Test
    public void testUnsuccessfulDiscovery() {
        CachedStrategies<Test> discovered = _testee.discoverImplementations(Test.class);

        // assertEquals(0, discovered.allAvailable().size());
        assertThat(discovered.allAvailable()).isEmpty();
    }

}
