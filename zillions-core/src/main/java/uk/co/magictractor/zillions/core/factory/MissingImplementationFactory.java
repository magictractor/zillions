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

import java.lang.reflect.Method;

/**
 * <p>
 * Creates a strategy implementation which throws an exception when any API
 * method is called.
 * </p>
 * <p>
 * One of these is returned by DefaultImplementationDiscovery when no
 * implementation can be found based on a system property and SPI.
 * </p>
 */
public class MissingImplementationFactory extends AbstractProxyImplementationFactory {

    @Override
    protected <T> Object handle(Class<T> apiClass, T defaultImplementation, Method method, Object[] args)
            throws Throwable {
        throw new UnsupportedOperationException(
            "No implementation found for " + apiClass.getSimpleName() + "  [method=" + method + "]");
    }

}
