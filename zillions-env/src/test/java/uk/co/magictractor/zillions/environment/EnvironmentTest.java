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
package uk.co.magictractor.zillions.environment;

// TODO! restore once code moved from zillions-core to zillions-junit
public class EnvironmentTest {

    //    @RegisterExtension
    //    public static SystemPropertiesExtension _systemProperties = new SystemPropertiesExtension();
    //    //.withProperty(SpiDiscoveryStrategy.class, "disabled", "false");
    //
    //    @RegisterExtension
    //    public static TestContextExtension _testContextRule = new TestContextExtension();
    //
    //    @Test
    //    public void testSystemPropertyIsAvailable() {
    //        String os = Environment.getProperties().getString("os.name", null);
    //        assertThat(os).isNotEmpty();
    //    }
    //
    //    @Test
    //    public void testUpdatedSystemPropertyIsCorrect() {
    //        String modified = "modified by unit test";
    //        _systemProperties.withProperty("os.name", modified);
    //
    //        String actual = Environment.getProperties().getString("os.name", null);
    //        assertThat(actual).isEqualTo(modified);
    //    }
    //
    //    // CurrencyNameProvider : https://www.baeldung.com/java-spi is abstract
    //    // maybe LogbackServletContainerInitializer
    //    @Test
    //    @Disabled("Failing - @Disabled while setting up Maven builds")
    //    public void testSpiFromJavaLibDirectory() {
    //        // TODO! need to turn off proxies
    //        CurrencyNameProvider impl = Environment.findImplementation(CurrencyNameProvider.class);
    //        // Assert.assertNotNull(impl);
    //        assertThat(impl).isNotNull();
    //    }
    //
    //    @Test
    //    @Disabled("Failing - @Disabled while setting up Maven builds")
    //    public void testSpiFromJavaLibExtDirectory() {
    //        FileSystemProvider impl = Environment.findImplementation(FileSystemProvider.class);
    //        assertThat(impl).isNotNull();
    //    }
    //
    //    @Test
    //    public void testSpiFromThisProject() {
    //        // TODO! bad test - this is a proxy rather than a proper SPI service load.
    //        CreateStrategy impl = Environment.findImplementation(CreateStrategy.class);
    //        assertThat(impl).isNotNull();
    //    }
    //
    //    // TODO! this implementation won't be portable
    //    // TODO! Fails when run in suite because state is not cleared after previous
    //    // tests
    //    @Test
    //    @Disabled("Failing - @Disabled while setting up Maven builds")
    //    public void testSPICanBeDisabledByProperty() {
    //        // FileSystemProvider impl =
    //        // testee.getImplementation(FileSystemProvider.class);
    //        System.setProperty("com.sun.nio.zipfs.ZipFileSystemProvider.disabled", "true");
    //
    //        FileSystemProvider actual = Environment.findImplementation(FileSystemProvider.class);
    //        // Assert.assertEquals(null, actual);
    //        assertThat(actual).isNull();
    //    }

}
