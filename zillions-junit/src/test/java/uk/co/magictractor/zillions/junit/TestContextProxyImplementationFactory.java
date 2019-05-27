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
package uk.co.magictractor.zillions.junit;

import uk.co.magictractor.zillions.core.factory.AbstractDelegatingImplementationFactory;

public class TestContextProxyImplementationFactory extends AbstractDelegatingImplementationFactory {

    @Override
    public <T> T createInstance(Class<T> apiClass, T defaultImplementation) {
        if (!TestContext.getInstance().isImplementationProxyEnabled()) {
            return defaultImplementation;
        }

        return super.createInstance(apiClass, defaultImplementation);
    }

    @Override
    protected <T> T findDelegate(Class<T> apiClass, T defaultImplementation) {
        // add debug statements?
        //      S impl = TestContext.getInstance().getTestImplementation(apiClass);
        //     return impl;

        System.err.println("delegate for: " + apiClass);

        // throw new UnsupportedOperationException("TODO");

        return defaultImplementation;
    }

}
