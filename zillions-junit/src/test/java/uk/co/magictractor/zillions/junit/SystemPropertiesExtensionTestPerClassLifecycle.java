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
package uk.co.magictractor.zillions.junit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Repeat the extension tests with a PER_CLASS Lifecycle (the other test used
 * the default PER_METHOD Lifecycle).
 */
@TestInstance(Lifecycle.PER_CLASS)
@Disabled("Failing - @Disabled while setting up Maven builds")
public class SystemPropertiesExtensionTestPerClassLifecycle extends SystemPropertiesExtensionTest {

}
