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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import com.google.common.base.MoreObjects;

import uk.co.magictractor.zillions.api.Init;
import uk.co.magictractor.zillions.api.factory.ImplementationFactory;
import uk.co.magictractor.zillions.core.factory.MissingImplementationFactory;
import uk.co.magictractor.zillions.environment.property.PropertyDiscovery;

/**
 * <ol>
 * <li>If there is a system property with name "{apiClassName}.impl" then the
 * system property's value is the class name of the api implementation to be
 * used. The implementation class is expected to have a public no-args
 * constructor.
 * </p>
 * <code>foo.bar.Interface.impl=foo.bar.Implementation</code>
 * <p>
 * </li>
 * <li>Check SPI for an implementation. If multiple implementations are found,
 * an exception is thrown.</li>
 * <li>If there is a DiscoveryFactory implementation it is given the opportunity
 * to create, wrap or replace the API class implementation.</li>
 * </ol>
 * This strategy is always used at least once, to find the Discovery strategy
 * and Property strategy implementations to be used. For those, if no
 * implementations for those are found, then DefaultDiscovery and DefaultPropery
 * are used.
 */
public class DefaultImplementationDiscovery implements ImplementationDiscovery {

    private final PropertyDiscovery _properties = initProperties();
    private final ImplementationFactory _factory = initFactory();
    private final MissingImplementationFactory _missingFactory = new MissingImplementationFactory();

    /**
     * Cache may contain null values for APIs where no implementing class was
     * found.
     */
    private final Map<Class<?>, Object> _cache = initCache();

    protected PropertyDiscovery initProperties() {
        return Environment.getProperties();
    }

    protected ImplementationFactory initFactory() {
        return Environment.getImplementationFactory();
    }

    protected Map<Class<?>, Object> initCache() {
        return new HashMap<>();
    }

    @Override
    public <T> T findImplementation(Class<T> apiClass) {
        T impl;
        if (_cache.containsKey(apiClass)) {
            impl = apiClass.cast(_cache.get(apiClass));
        }
        else {
            impl = discover(apiClass);
            if (impl != null) {
                init(impl);
            }
            _cache.put(apiClass, impl);
        }

        return impl;
    }

    private <T> T discover(Class<T> apiClass) {
        T impl;

        impl = discoverImplementationViaSystemProperty(apiClass);

        if (impl == null) {
            impl = discoverImplementationViaSpi(apiClass);
        }

        /**
         * Call factory even if an impl has been found. The factory may replace
         * it, or wrap it, or return it unmodified.
         */
        if (_factory != null) {
            impl = _factory.createInstance(apiClass, impl);
        }

        if (impl == null) {
            impl = createMissingImplementation(apiClass);
        }

        return impl;
    }

    private <T> void init(T impl) {
        if (impl instanceof Init) {
            ((Init) impl).init();
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T discoverImplementationViaSystemProperty(Class<T> apiClass) {
        String implName = _properties.getString(apiClass, "impl", null);
        if (implName == null) {
            return null;
        }

        try {
            Class<?> implClass = Class.forName(implName);
            return (T) implClass.newInstance();
        }
        catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    protected <T> T discoverImplementationViaSpi(Class<T> apiClass) {
        T impl = null;
        List<T> spiList = spiLoadList(apiClass);
        if (spiList.size() == 1) {
            impl = spiList.get(0);
        }
        else if (spiList.size() > 1) {
            // TODO! exceptions which point at a web page containing more information
            throw new IllegalStateException("There are multiple SPI implementations for " + apiClass.getName()
                    + ". The project is either misconfigured, or should use a system property to specify the implementation to be used.");
        }

        return impl;
    }

    protected <T> T createMissingImplementation(Class<T> apiClass) {
        return _missingFactory.createInstance(apiClass, null);
    }

    protected <T> List<T> spiLoadList(Class<T> apiClass) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(apiClass);
        List<T> spiList = new ArrayList<>();
        serviceLoader.iterator().forEachRemaining(i -> spiList.add(i));
        return spiList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

}
