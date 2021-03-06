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
package uk.co.magictractor.zillions.core.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.magictractor.zillions.api.factory.ImplementationFactory;

public abstract class AbstractProxyImplementationFactory implements ImplementationFactory {

    private final Logger _logger = LoggerFactory.getLogger(getClass());

    // Perhaps allow null values for marker interfaces
    // TODO! would be ropey with super interfaces
    private Map<Class<?>, Supplier<?>> _interfaceValueSuppliers = new HashMap<>();

    // Only a no-args constructor because strategy factories are generally wired in
    // via SPI
    public AbstractProxyImplementationFactory() {
    }

    protected Logger getLogger() {
        return _logger;
    }

    protected void addInterface(Class<?> interfaceClass, Supplier<?> interfaceValueSupplier) {
        // TODO! assert that the interface class is a marker interface or functional
        // interface and that it does not have a superclass
        _interfaceValueSuppliers.put(interfaceClass, interfaceValueSupplier);
    }

    @Override
    public <T> T createInstance(Class<T> apiClass, T defaultImplementation) {

        if (!apiClass.isInterface()) {
            throw new IllegalArgumentException(
                "API class is not an interface so cannot be proxied: " + apiClass.getName());
        }

        ClassLoader classLoader = AbstractProxyImplementationFactory.class.getClassLoader();

        Class<?>[] proxyInterfaces = new Class<?>[_interfaceValueSuppliers.size() + 1];
        proxyInterfaces[0] = apiClass;
        int i = 1;
        for (Class<?> interfaceClass : _interfaceValueSuppliers.keySet()) {
            proxyInterfaces[i++] = interfaceClass;
        }

        Object proxy = Proxy.newProxyInstance(classLoader, proxyInterfaces,
            new StrategyFactoryInvocationHandler<>(apiClass, defaultImplementation));

        return apiClass.cast(proxy);
    }

    private class StrategyFactoryInvocationHandler<T> implements InvocationHandler {

        private final Class<T> _apiClass;
        private final T _defaultImplementation;

        StrategyFactoryInvocationHandler(Class<T> apiClass, T defaultImplementation) {
            _apiClass = apiClass;
            _defaultImplementation = defaultImplementation;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if ("toString".equals(method.getName()) && args == null) {
                return handleToString(_apiClass);
            }

            if (_interfaceValueSuppliers.containsKey(method.getDeclaringClass())) {
                return _interfaceValueSuppliers.get(method.getDeclaringClass()).get();
            }

            try {
                return handle(_apiClass, _defaultImplementation, method, args);
            }
            catch (InvocationTargetException e) {
                // Unwrap and rethrow original exception.
                throw e.getCause();
            }
        }
    }

    protected abstract <T> Object handle(Class<T> apiClass, T defaultImplementation, Method method, Object[] args)
            throws Throwable;

    protected String handleToString(Class<?> apiClass) {
        // TODO! MoreObjects.toStringHelper
        return getClass().getSimpleName() + "[apiClass=" + apiClass.getName() + "]";
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

}
