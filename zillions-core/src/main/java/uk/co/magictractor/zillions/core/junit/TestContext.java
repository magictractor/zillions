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
package uk.co.magictractor.zillions.core.junit;

import java.util.ArrayList;
import java.util.List;

import uk.co.magictractor.zillions.core.api.Strategies;
import uk.co.magictractor.zillions.core.api.StrategiesFactory;
import uk.co.magictractor.zillions.core.environment.CachedStrategies;

/**
 * This class is not public, to ensure that unit tests use {@link TestContextExtension} to
 * modify the TestContext, which will also result in changes to the context being reverted
 * at the end of the test.
 */
/* default */ final class TestContext {

    private static TestContext __instance = new TestContext();

    private List<Object> _testImplementations = new ArrayList<Object>();

    private TestContext() {
    }

    public static TestContext getInstance() {
        return __instance;
    }

    public <S> Strategies<S> getImplementations(Class<S> apiClass, StrategiesFactory implementations) {
        CachedStrategies<S> strategies = new CachedStrategies<S>(apiClass);

        for (Object candidate : _testImplementations) {
            if (apiClass.isInstance(candidate)) {
                strategies.addStrategyImplementation(apiClass.cast(candidate));
            }
        }

        return strategies;
    }

    private <S> List<S> getTestImplementations(Class<S> apiClass) {
        List<S> result = new ArrayList<S>();
        for (Object candidate : _testImplementations) {
            if (apiClass.isInstance(candidate)) {
                result.add(apiClass.cast(candidate));
            }
        }
        return result;
    }

    public <S> S getTestImplementation(Class<S> apiClass) {
        List<S> implementations = getTestImplementations(apiClass);
        return implementations.isEmpty() ? null : implementations.get(0);
    }

    public void addImplementation(Object implementation) {
        if (implementation instanceof Class) {
            _testImplementations.add(newInstance((Class<?>) implementation));
        }
        else {
            _testImplementations.add(implementation);
        }
    }

    // TODO! this suggests having a StrategyListMap field
    private Object newInstance(Class<?> implementationClass) {
        try {
            return implementationClass.newInstance();
        }
        catch (Exception e) {
            String msg = "Unable to construct instance of " + implementationClass.getSimpleName();
            throw new IllegalStateException(msg, e);
        }
    }

    public void reset() {
        _testImplementations.clear();
    }

    public boolean hasImplementation() {
        return !_testImplementations.isEmpty();
    }

}
