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

import java.nio.charset.spi.CharsetProvider;
import java.nio.file.spi.FileSystemProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import uk.co.magictractor.zillions.core.create.CreateStrategy;
import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.junit.SystemProperties;
import uk.co.magictractor.zillions.core.junit.TestContextRule;

public class EnvironmentTest
{

  // @Rule
  @Rule
  public SystemProperties _systemProperties = new SystemProperties();

  @ClassRule
  public static TestContextRule _testContextRule = new TestContextRule();

  @Before
  public void setUp() {
    // uk.co.magictractor.math.big.discovery.SpiDiscoveryStrategy.disabled=true
    // uk.co.magictractor.math.big.implementation.ImplementationStrategy.impl=uk.co.magictractor.math.big.implementation.UnitTestImplementationStrategy
    // _systemProperties.clearProperty("uk.co.magictractor.math.big.discovery.SpiDiscoveryStrategy.disabled");
    // _systemProperties.clearProperty("uk.co.magictractor.math.big.implementation.ImplementationStrategy.impl");

    // _systemProperties.setProperty("uk.co.magictractor.math.big.property.FilePropertyStrategy.disabled",
    // "true");

    _systemProperties.setProperty("uk.co.magictractor.math.big.implementation.ImplementationStrategy.impl", "false");

    _systemProperties.setProperty("uk.co.magictractor.math.big.discovery.SpiDiscoveryStrategy.disabled", "false");

    // _testContextRule.enableSpi();
  }

  @Test
  public void testSystemPropertyIsAvailable() {
    String os = Environment.getProperties().getString("os.name", null);
    Assert.assertNotNull(os);
  }

  @Test
  public void testUpdatedSystemPropertyIsCorrect() {
    String modified = "modified by unit test";
    _systemProperties.setProperty("os.name", modified);

    String actual = Environment.getProperties().getString("os.name", null);
    Assert.assertEquals(modified, actual);
  }

  @Test
  public void testSpiFromJavaLibDirectory() {
    CharsetProvider impl = Environment.getImplementation(CharsetProvider.class);
    Assert.assertNotNull(impl);
  }

  @Test
  public void testSpiEnabled() {
    String actual = Environment.getProperties().getString("uk.co.magictractor.math.big.discovery.SpiDiscoveryStrategy.disabled");
    Assert.assertEquals("false", actual);
  }

  @Test
  public void testSpiFromJavaLibExtDirectory() {
    FileSystemProvider impl = Environment.getImplementation(FileSystemProvider.class);
    Assert.assertNotNull(impl);
  }

  @Test
  public void testSpiFromThisProject() {
    CreateStrategy impl = Environment.getImplementation(CreateStrategy.class);
    Assert.assertNotNull(impl);
  }

  // TODO! this implementation won't be portable
  // TODO! Fails when run in suite because state is not cleared after previous tests
  @Test
  public void testSPICanBeDisabledByProperty() {
    // FileSystemProvider impl =
    // testee.getImplementation(FileSystemProvider.class);
    System.setProperty("com.sun.nio.zipfs.ZipFileSystemProvider.disabled", "true");

    FileSystemProvider actual = Environment.getImplementation(FileSystemProvider.class);
    Assert.assertEquals(null, actual);
  }

}
