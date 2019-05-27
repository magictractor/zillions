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

import java.util.function.Supplier;

import uk.co.magictractor.zillions.api.factory.ImplementationFactory;
import uk.co.magictractor.zillions.environment.property.DefaultPropertyDiscovery;
import uk.co.magictractor.zillions.environment.property.PropertyDiscovery;

public final class Environment {

    private static final PropertyDiscovery PROPERTY_DISCOVERY;
    private static final ImplementationDiscovery IMPLEMENTATION_DISCOVERY;
    private static final ImplementationFactory IMPLEMENTATION_FACTORY;

    static {
        DefaultImplementationDiscovery bootstrapDiscovery = new BootstrapImplementationDiscovery();
        PROPERTY_DISCOVERY = coalesce(bootstrapDiscovery.findImplementation(PropertyDiscovery.class),
            DefaultPropertyDiscovery::new);
        IMPLEMENTATION_FACTORY = bootstrapDiscovery.findImplementation(ImplementationFactory.class);
        // ImplementationDiscovery last because most implementations refer to the previous values.
        IMPLEMENTATION_DISCOVERY = coalesce(bootstrapDiscovery.findImplementation(ImplementationDiscovery.class),
            DefaultImplementationDiscovery::new);
    }

    private Environment() {
    }

    public static <T> T findImplementation(Class<T> apiClass) {
        return IMPLEMENTATION_DISCOVERY.findImplementation(apiClass);
    }

    public static PropertyDiscovery getProperties() {
        return PROPERTY_DISCOVERY;
    }

    /* default */ static ImplementationFactory getImplementationFactory() {
        return IMPLEMENTATION_FACTORY;
    }

    private static <T> T coalesce(T discovered, Supplier<T> defaultSupplier) {
        return discovered == null ? defaultSupplier.get() : discovered;
    }

}
