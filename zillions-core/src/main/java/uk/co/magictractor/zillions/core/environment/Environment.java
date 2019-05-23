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
package uk.co.magictractor.zillions.core.environment;

import uk.co.magictractor.zillions.core.discovery.BootstrapImplementationDiscovery;
import uk.co.magictractor.zillions.core.discovery.DefaultImplementationDiscovery;
import uk.co.magictractor.zillions.core.discovery.ImplementationDiscovery;
import uk.co.magictractor.zillions.core.property.DefaultPropertyDiscovery;
import uk.co.magictractor.zillions.core.property.PropertyDiscovery;

public final class Environment {

    private static final ImplementationDiscovery IMPLEMENTATION_DISCOVERY;
    private static final PropertyDiscovery PROPERTY_DISCOVERY;

    static {
        DefaultImplementationDiscovery bootstrapDiscovery = new BootstrapImplementationDiscovery();
        PROPERTY_DISCOVERY = bootstrapDiscovery.findOptionalImplementation(PropertyDiscovery.class)
                .orElseGet(DefaultPropertyDiscovery::new);
        IMPLEMENTATION_DISCOVERY = bootstrapDiscovery.findOptionalImplementation(ImplementationDiscovery.class)
                .orElseGet(DefaultImplementationDiscovery::new);
    }

    private Environment() {
    }

    public static <T> T findImplementation(Class<T> apiClass) {
        return IMPLEMENTATION_DISCOVERY.findImplementation(apiClass);
    }

    public static PropertyDiscovery getProperties() {
        return PROPERTY_DISCOVERY;
    }

}
