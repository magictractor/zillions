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
package uk.co.magictractor.zillions.core.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.magictractor.zillions.core.discovery.DiscoveryStrategy;
import uk.co.magictractor.zillions.core.environment.CachedStrategies;
import uk.co.magictractor.zillions.core.environment.StrategyFactory;

public class StrategiesFactory {
    private final Logger _logger = LoggerFactory.getLogger(getClass());

    private final Map<Class<?>, Strategies<?>> _implementationMap = new HashMap<Class<?>, Strategies<?>>();

    public StrategiesFactory() {
        // ?
        // _implementationMap.put(PropertyStrategy.class, new PropertyStrategyList());
        _implementationMap.put(DiscoveryStrategy.class,
            new CachedStrategies<DiscoveryStrategy>(DiscoveryStrategy.class));
    }

    public <S> Strategies<S> getStrategyList(Class<S> apiClass) {
        return getStrategyList(apiClass, true);
    }

    public <S> Strategies<S> getStrategyListWithoutDiscovery(Class<S> apiClass) {
        return getStrategyList(apiClass, false);
    }

    private <S> Strategies<S> getStrategyList(Class<S> apiClass, boolean allowDiscovery) {
        System.out.println("discover: " + apiClass + ", " + allowDiscovery);

        Strategies<S> implementations;
        if (!_implementationMap.containsKey(apiClass)) {
            // TODO! tidy this (if it works)
            implementations = allowDiscovery ? discoverStrategyList(apiClass) : new CachedStrategies<S>(apiClass);
            _implementationMap.put(apiClass, implementations);
        }
        else {
            implementations = (Strategies<S>) _implementationMap.get(apiClass);
        }

        return implementations;
    }

    private <S> Strategies<S> discoverStrategyList(Class<S> apiClass) {

        CachedStrategies<S> implementations = new CachedStrategies<S>(apiClass);

        // Give every known StrategyFactory an opportunity to create an instance of the
        // api.
        if (apiClass != StrategyFactory.class) {
            // TODO! temporary workaround for empty StrategyFactory list being cached.
            // ?!? bad smell
            _implementationMap.remove(StrategyFactory.class);
            for (StrategyFactory strategyFactory : getStrategyList(StrategyFactory.class).allAvailable()) {
                S strategy = strategyFactory.createInstance(apiClass);
                if (strategy != null) {
                    implementations.addStrategyImplementation(strategy);
                }
            }
        }

        for (DiscoveryStrategy discoveryStrategy : allAvailableDiscoveryStrategies()) {
            implementations.addStrategies(discoveryStrategy.discoverImplementations(apiClass));
        }

        if (implementations.allAvailable().isEmpty()) {
            _logger.warn("No available implementation found for {}", apiClass.getName());
        }

        return implementations;
    }

    @SuppressWarnings("unchecked")
    private List<DiscoveryStrategy> allAvailableDiscoveryStrategies() {
        Strategies<DiscoveryStrategy> discoveryStrategies = (Strategies<DiscoveryStrategy>) _implementationMap.get(
            DiscoveryStrategy.class);
        return discoveryStrategies.allAvailable();
    }

}
