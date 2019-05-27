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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is not public, to ensure that unit tests use
 * {@link TestContextExtension} to modify the TestContext, which will also
 * result in changes to the context being reverted at the end of the test.
 */
/* default */ final class TestContext {

    private static TestContext INSTANCE = new TestContext();

    private final List<Object> _testImplementations = new ArrayList<>();
    //     TODO! better name
    private boolean _isImplementationProxyEnabled = true;

    public static TestContext getInstance() {
        return INSTANCE;
    }

    private TestContext() {
    }

    public boolean isImplementationProxyEnabled() {
        return _isImplementationProxyEnabled;
    }

    public void setImplementationProxyEnabled(boolean isImplementationProxyEnabled) {
        _isImplementationProxyEnabled = isImplementationProxyEnabled;
    }

    public List<Object> getTestImplementations() {
        return _testImplementations;
    }

    private <S> List<S> getTestImplementations(Class<S> apiClass) {
        return _testImplementations.stream()
                .filter(apiClass::isInstance)
                .map(apiClass::cast)
                .collect(Collectors.toList());
    }

    public <S> S getTestImplementation(Class<S> apiClass) {
        List<S> implementations = getTestImplementations(apiClass);
        if (implementations.size() > 1) {
            throw new IllegalStateException("Multiple implementations available for " + apiClass.getName());
        }
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

    // TODO! util for this? - use JUnit ReflectionSupport?
    private static Object newInstance(Class<?> implementationClass) {
        try {
            return implementationClass.newInstance();
        }
        // TODO! more specific exception
        catch (Exception e) {
            String msg = "Unable to construct instance of " + implementationClass.getSimpleName();
            throw new IllegalStateException(msg, e);
        }
    }

    //    public boolean hasImplementation() {
    //        return !_testImplementations.isEmpty();
    //    }

}
