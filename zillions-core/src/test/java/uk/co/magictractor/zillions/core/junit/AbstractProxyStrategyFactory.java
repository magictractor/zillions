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
package uk.co.magictractor.zillions.core.junit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import uk.co.magictractor.zillions.core.environment.StrategyFactory;

public abstract class AbstractProxyStrategyFactory implements StrategyFactory {

	public AbstractProxyStrategyFactory() {
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
		EnvironmentDelegatingHandler handler = new EnvironmentDelegatingHandler(apiClass);

		return (S) Proxy.newProxyInstance(classLoader, new Class[] { apiClass }, handler);
	}

	abstract <S> S findDelegate(Class<S> apiClass);

	private final class EnvironmentDelegatingHandler implements InvocationHandler {
		private Class<?> _apiClass;

		private EnvironmentDelegatingHandler(Class<?> apiClass) {
			_apiClass = apiClass;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			Object delegate = findDelegate(_apiClass);

			try {
				return method.invoke(delegate, args);
			} catch (InvocationTargetException e) {
				// Unwrap and rethrow original exception.
				throw e.getCause();
			}
		}
	}

}
