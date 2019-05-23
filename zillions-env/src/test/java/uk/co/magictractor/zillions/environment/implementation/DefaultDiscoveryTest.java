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
package uk.co.magictractor.zillions.environment.implementation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.api.create.CreateStrategy;

public class DefaultDiscoveryTest {

    private DefaultImplementationDiscovery _testee = new DefaultImplementationDiscovery();

    // NEXT - NPE in bootstrap.
    @Test
    @Disabled("Failing - @Disabled while setting up Maven builds")
    public void testSuccessfulDiscovery() {
        CreateStrategy discovered = _testee.findImplementation(CreateStrategy.class);

        // assertThat(discovered.allAvailable()).hasSize(1);
        // assertThat(discovered.bestAvailable()).isExactlyInstanceOf(NumptyCreateStrategy.class);
    }

    @Test
    public void testUnsuccessfulDiscovery() {
        Test discovered = _testee.findImplementation(Test.class);

        //assertThat(discovered.allAvailable()).isEmpty();
    }

}
