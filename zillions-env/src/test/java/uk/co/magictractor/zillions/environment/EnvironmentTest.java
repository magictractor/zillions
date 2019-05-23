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

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.spi.FileSystemProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.engine.TestEngine;

import uk.co.magictractor.zillions.environment.dummy.Single;
import uk.co.magictractor.zillions.environment.dummy.SingleOne;
import uk.co.magictractor.zillions.environment.dummy.SingleOther;
import uk.co.magictractor.zillions.junit.SystemPropertiesExtension;
import uk.co.magictractor.zillions.junit.TestContextExtension;

//  TODO! will need to turn off proxies here once they have been restored for other tests
public class EnvironmentTest {

    @RegisterExtension
    public static SystemPropertiesExtension _systemProperties = new SystemPropertiesExtension();

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

    @Test
    public void testSpiFromJava() {
        FileSystemProvider impl = Environment.findImplementation(FileSystemProvider.class);
        // Don't check the exact type, it will vary depending on Java versions and platforms.
        assertThat(impl).isNotNull();
    }

    @Test
    public void testSpiFromThirdPartyLib() {
        TestEngine impl = Environment.findImplementation(TestEngine.class);
        assertThat(impl).isExactlyInstanceOf(JupiterTestEngine.class);
    }

    @Test
    public void testSpiFromThisProject() {
        Single impl = Environment.findImplementation(Single.class);
        assertThat(impl).isExactlyInstanceOf(SingleOne.class);
    }

    @Test
    public void testPropertyOverridesSpi() {
        _systemProperties.withProperty(Single.class, "impl", SingleOther.class.getName());

        Single impl = Environment.findImplementation(Single.class);
        assertThat(impl).isExactlyInstanceOf(SingleOther.class);
    }
}
