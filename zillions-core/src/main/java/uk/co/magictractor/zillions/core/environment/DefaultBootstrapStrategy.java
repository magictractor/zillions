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

import uk.co.magictractor.zillions.core.api.StrategiesFactory;
import uk.co.magictractor.zillions.core.discovery.DiscoveryStrategy;
import uk.co.magictractor.zillions.core.discovery.ImplPropertyDiscoveryStrategy;
import uk.co.magictractor.zillions.core.discovery.SpiDiscoveryStrategy;
import uk.co.magictractor.zillions.core.property.FilePropertyStrategy;
import uk.co.magictractor.zillions.core.property.PropertyStrategy;
import uk.co.magictractor.zillions.core.property.SystemPropertyStrategy;

public class DefaultBootstrapStrategy implements BootstrapStrategy {

    @Override
    public void bootstrap(StrategiesFactory implementations) {

        bootstrapPropertyStrategies(implementations);
        bootstrapDiscoveryStrategies(implementations);

        // TODO! discover other property and discovery strategies.
        for (DiscoveryStrategy discoveryStrategy : implementations.getStrategyList(
            DiscoveryStrategy.class).allAvailable()) {
            discoverOtherStrategies(implementations, discoveryStrategy);
        }
    }

    private void bootstrapPropertyStrategies(StrategiesFactory implementations) {
        // TODO! refactor - at least get rid of casting
        // TODO! why get this list? could just add impl?
        CachedStrategies<PropertyStrategy> propertyStrategies = (CachedStrategies) implementations.getStrategyListWithoutDiscovery(
            PropertyStrategy.class);

        propertyStrategies.addStrategyImplementation(new SystemPropertyStrategy(),
            StrategyOption.SKIP_EXPLICIT_DISABLED_CHECK);

        // TODO! allow these to be renamed and disabled (subclasses of FilePropertyStrategy?)
        propertyStrategies.addStrategyImplementation(new FilePropertyStrategy("/strategies.properties"));
        propertyStrategies.addStrategyImplementation(new FilePropertyStrategy("/test-strategies.properties"));
    }

    private void bootstrapDiscoveryStrategies(StrategiesFactory implementations) {
        // TODO! refactor - at least get rid of casting
        CachedStrategies<DiscoveryStrategy> discoveryStrategies = (CachedStrategies) implementations.getStrategyListWithoutDiscovery(
            DiscoveryStrategy.class);

        discoveryStrategies.addStrategyImplementation(new ImplPropertyDiscoveryStrategy());
        discoveryStrategies.addStrategyImplementation(new SpiDiscoveryStrategy());
    }

    // hmm over engineering? is this used? - should look for other PropertyStrategies though?
    // TODO! discovery using discovered strategies
    // TODO! guard against infinite loops
    private void discoverOtherStrategies(StrategiesFactory implementations, DiscoveryStrategy discoveryStrategy) {
        // TODO! refactor - at least get rid of casting
        ((CachedStrategies) implementations.getStrategyList(PropertyStrategy.class)).addStrategies(
            discoveryStrategy.discoverImplementations(PropertyStrategy.class));
        ((CachedStrategies) implementations.getStrategyList(DiscoveryStrategy.class)).addStrategies(
            discoveryStrategy.discoverImplementations(DiscoveryStrategy.class));
    }

}
