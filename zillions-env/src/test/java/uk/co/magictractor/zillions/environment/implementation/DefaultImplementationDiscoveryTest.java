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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ServiceConfigurationError;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.environment.dummy.Missing;
import uk.co.magictractor.zillions.environment.dummy.Multiple;
import uk.co.magictractor.zillions.environment.dummy.NotSubtype;
import uk.co.magictractor.zillions.environment.dummy.Single;
import uk.co.magictractor.zillions.environment.dummy.SingleOne;

public class DefaultImplementationDiscoveryTest {

    private DefaultImplementationDiscovery _testee = new DefaultImplementationDiscovery();

    @Test
    public void testSuccessfulDiscovery() {
        Single discovered = _testee.findImplementation(Single.class);

        assertThat(discovered).isExactlyInstanceOf(SingleOne.class);
    }

    @Test
    public void testUnsuccessfulDiscoveryNoImplementation() {
        assertThatThrownBy(() -> _testee.findImplementation(Test.class))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("No implementation found for interface org.junit.jupiter.api.Test");
    }

    @Test
    public void testUnsuccessfulDiscoveryMultipleImplementations() {
        assertThatThrownBy(() -> _testee.findImplementation(Multiple.class))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage(
                    "There are multiple SPI implementations for uk.co.magictractor.zillions.environment.dummy.Multiple. The project is either misconfigured, or should use a system property to specify the implementation to be used.");
    }

    /** The SPI file refers to a class which does not exist. */
    @Test
    public void testUnsuccessfulDiscoveryMissingImplementation() {
        assertThatThrownBy(() -> _testee.findImplementation(Missing.class))
                .isExactlyInstanceOf(ServiceConfigurationError.class)
                .hasMessage(
                    "uk.co.magictractor.zillions.environment.dummy.Missing: Provider uk.co.magictractor.zillions.environment.dummy.DoesNotExist not found");
    }

    /**
     * The SPI file refers to a class which exists but does not implement the
     * API interface.
     */
    @Test
    public void testUnsuccessfulDiscoveryNotSubtype() {
        assertThatThrownBy(() -> _testee.findImplementation(NotSubtype.class))
                .isExactlyInstanceOf(ServiceConfigurationError.class)
                .hasMessage(
                    "uk.co.magictractor.zillions.environment.dummy.NotSubtype: Provider uk.co.magictractor.zillions.environment.dummy.SingleOne not a subtype");
    }
}
