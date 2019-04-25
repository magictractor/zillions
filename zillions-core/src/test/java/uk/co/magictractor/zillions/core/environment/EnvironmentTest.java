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
package uk.co.magictractor.zillions.core.environment;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.spi.FileSystemProvider;
import java.util.spi.CurrencyNameProvider;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import uk.co.magictractor.zillions.core.create.CreateStrategy;
import uk.co.magictractor.zillions.core.discovery.SpiDiscoveryStrategy;
import uk.co.magictractor.zillions.core.junit.SystemPropertiesExtension;
import uk.co.magictractor.zillions.core.junit.TestContextExtension;

public class EnvironmentTest {

    @RegisterExtension
    public static SystemPropertiesExtension _systemProperties = new SystemPropertiesExtension().withProperty(
        SpiDiscoveryStrategy.class, "disabled", "false");

    @RegisterExtension
    public static TestContextExtension _testContextRule = new TestContextExtension();

    @Test
    public void testSystemPropertyIsAvailable() {
        String os = Environment.getProperties().getString("os.name", null);
        assertThat(os).isNotEmpty();
    }

    @Test
    public void testUpdatedSystemPropertyIsCorrect() {
        String modified = "modified by unit test";
        _systemProperties.withProperty("os.name", modified);

        String actual = Environment.getProperties().getString("os.name", null);
        assertThat(actual).isEqualTo(modified);
    }

    // CurrencyNameProvider : https://www.baeldung.com/java-spi is abstract
    // maybe LogbackServletContainerInitializer
    @Test
    @Disabled("Failing - @Disabled while setting up Maven builds")
    public void testSpiFromJavaLibDirectory() {
        // TODO! need to turn off proxies
        CurrencyNameProvider impl = Environment.getBestAvailableImplementation(CurrencyNameProvider.class);
        // Assert.assertNotNull(impl);
        assertThat(impl).isNotNull();
    }

    @Test
    public void testSpiEnabled() {
        String actual = Environment.getProperties().getString(
            "uk.co.magictractor.zillions.core.discovery.SpiDiscoveryStrategy.disabled");
        assertThat(actual).isEqualTo("false");
    }

    @Test
    @Disabled("Failing - @Disabled while setting up Maven builds")
    public void testSpiFromJavaLibExtDirectory() {
        FileSystemProvider impl = Environment.getBestAvailableImplementation(FileSystemProvider.class);
        assertThat(impl).isNotNull();
    }

    @Test
    public void testSpiFromThisProject() {
        // TODO! bad test - this is a proxy rather than a proper SPI service load.
        CreateStrategy impl = Environment.getBestAvailableImplementation(CreateStrategy.class);
        assertThat(impl).isNotNull();
    }

    // TODO! this implementation won't be portable
    // TODO! Fails when run in suite because state is not cleared after previous
    // tests
    @Test
    @Disabled("Failing - @Disabled while setting up Maven builds")
    public void testSPICanBeDisabledByProperty() {
        // FileSystemProvider impl =
        // testee.getImplementation(FileSystemProvider.class);
        System.setProperty("com.sun.nio.zipfs.ZipFileSystemProvider.disabled", "true");

        FileSystemProvider actual = Environment.getBestAvailableImplementation(FileSystemProvider.class);
        // Assert.assertEquals(null, actual);
        assertThat(actual).isNull();
    }

}
