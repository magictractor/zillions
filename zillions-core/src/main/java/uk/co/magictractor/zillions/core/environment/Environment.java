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

import uk.co.magictractor.zillions.core.api.Strategies;
import uk.co.magictractor.zillions.core.api.StrategiesFactory;
import uk.co.magictractor.zillions.core.property.PropertyDecorator;
import uk.co.magictractor.zillions.core.property.PropertyStrategy;

public final class Environment {

    private static final StrategiesFactory STRATEGY_FACTORY = new StrategiesFactory();
    private static final PropertyDecorator PROPERTIES;

    static {
        // PROPERTY_LIST = (PropertyStrategyList)
        // STRATEGY_FACTORY.getStrategyList(PropertyStrategy.class);

        /*
         * Custom bootstrapping should never be required but can be done. It's intended
         * that sufficient customisation is using the default bootstrap.
         */
        String bootstrapClassName = System.getProperty(Environment.class.getName() + ".bootstrap");
        BootstrapStrategy bootstrapStrategy;
        if (bootstrapClassName == null) {
            bootstrapStrategy = new DefaultBootstrapStrategy();
        }
        else {
            bootstrapStrategy = safeLoadBootstrapClass(bootstrapClassName);
        }

        PROPERTIES = new PropertyDecorator(STRATEGY_FACTORY.getStrategyListWithoutDiscovery(PropertyStrategy.class));

        bootstrapStrategy.bootstrap(STRATEGY_FACTORY);
    }

    private Environment() {
    }

    private static BootstrapStrategy safeLoadBootstrapClass(String bootstrapClassName) {
        try {
            return loadBootstrapClass(bootstrapClassName);
        }
        catch (Exception e) {
            // TODO! what to do now?
            // Perhaps wire in special strategies.
            System.err.println("Could not load " + bootstrapClassName);
            e.printStackTrace(System.err);
            return null;
        }
    }

    private static BootstrapStrategy loadBootstrapClass(String bootstrapClassName) throws ReflectiveOperationException {
        Class<?> bootstrapClass = Class.forName(bootstrapClassName);
        return (BootstrapStrategy) bootstrapClass.newInstance();
    }

    public static <S> S getBestAvailableImplementation(Class<S> apiClass) {
        return getStrategyList(apiClass).bestAvailable();
    }

    //    public static <S> List<S> getAvailableImplementations(Class<S> apiClass) {
    //        return getStrategyList(apiClass).allAvailable();
    //    }

    // TODO! bin or move
    public static void debugImplementations(Class<?> apiClass) {
        System.err.println("All implementations of " + apiClass.getSimpleName());
        getStrategyList(apiClass).strategyHolders().forEach(System.err::println);
        System.err.println("-------------------------");
    }

    public static <S> Strategies<S> getStrategyList(Class<S> apiClass) {
        return STRATEGY_FACTORY.getStrategyList(apiClass);
    }

    public static StrategiesFactory getImplementations() {
        return STRATEGY_FACTORY;
    }

    public static PropertyDecorator getProperties() {
        return PROPERTIES;
    }

}
