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

import uk.co.magictractor.zillions.api.factory.ImplementationFactory;
import uk.co.magictractor.zillions.environment.property.DefaultPropertyDiscovery;
import uk.co.magictractor.zillions.environment.property.PropertyDiscovery;

/**
 * ImplementationDiscovery implementation which is used once at startup to find
 * the ImplementationDiscovery and PropertyDiscovery implementations.
 */
public class BootstrapImplementationDiscovery extends DefaultImplementationDiscovery {

    @Override
    protected PropertyDiscovery initProperties() {
        return new DefaultPropertyDiscovery();
    }

    @Override
    protected ImplementationFactory initFactory() {
        // No ImplementationFactory during bootstrap.
        return null;
    }

    @Override
    protected <T> T createMissingImplementation(Class<T> apiClass) {
        return null;
    }

}
