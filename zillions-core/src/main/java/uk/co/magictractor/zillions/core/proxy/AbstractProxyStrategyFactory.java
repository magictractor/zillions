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
package uk.co.magictractor.zillions.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.base.MoreObjects;

import uk.co.magictractor.zillions.core.environment.StrategyFactory;

public abstract class AbstractProxyStrategyFactory implements StrategyFactory {

	// Perhaps allow null values for marker interfaces
	// TODO! would be ropey with super interfaces
	private Map<Class<?>, Supplier<?>> _interfaceValueSuppliers = new HashMap<>();

	// Only a no-args constructor because strategy factories are generally wired in
	// via SPI
	public AbstractProxyStrategyFactory() {
	}

	protected void addInterface(Class<?> interfaceClass, Supplier<?> interfaceValueSupplier) {
		// TODO! assert that the interface class is a marker interface or functional
		// interface
		// and that it does not have a superclass
		_interfaceValueSuppliers.put(interfaceClass, interfaceValueSupplier);
	}

	public <S> S createInstance(Class<S> apiClass) {

		// TODO! change tests so that this isn't required
		// Some tests use Java SPI classes which are abstract without an interface
		if (!apiClass.isInterface()) {
			System.err.println("API class is not an interface so cannot be proxied: " + apiClass.getName());
			// return apiCla
			throw new IllegalArgumentException(
					"API class is not an interface so cannot be proxied: " + apiClass.getName());
		}

		ClassLoader classLoader = AbstractProxyStrategyFactory.class.getClassLoader();

		Class<?>[] proxyInterfaces = new Class<?>[_interfaceValueSuppliers.size() + 1];
		proxyInterfaces[0] = apiClass;
		int i = 1;
		for (Class<?> interfaceClass : _interfaceValueSuppliers.keySet()) {
			proxyInterfaces[i++] = interfaceClass;
		}

		return (S) Proxy.newProxyInstance(classLoader, proxyInterfaces, new StrategyFactoryInvocationHandler(apiClass));
	}

	private class StrategyFactoryInvocationHandler implements InvocationHandler {

		private final Class<?> _apiClass;

		StrategyFactoryInvocationHandler(Class<?> apiClass) {
			this._apiClass = apiClass;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			if ("toString".equals(method.getName()) && args == null) {
				return handleToString(_apiClass);
			}

			// Priority etc are delegated to this factory/
			if (_interfaceValueSuppliers.containsKey(method.getDeclaringClass())) {
				System.err.println("interface: " + method.getDeclaringClass().getSimpleName());
				return _interfaceValueSuppliers.get(method.getDeclaringClass()).get();
			}

			// System.err.println("proxy: " + proxy);
			System.err.println("method: " + method);

			try {
				return handle(_apiClass, method, args);
			} catch (InvocationTargetException e) {
				// Unwrap and rethrow original exception.
				throw e.getCause();
			}
		}
	}

	abstract Object handle(Class<?> apiClass, Method method, Object[] args) throws Throwable;

	protected String handleToString(Class<?> apiClass) {
		// TODO! ToStringBuilder
		return getClass().getSimpleName() + "[apiClass=" + apiClass.getName() + "]";
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).toString();
	}
}
