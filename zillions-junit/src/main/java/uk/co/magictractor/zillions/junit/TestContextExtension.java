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
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.extension.ExtensionContext;

public class TestContextExtension extends EnsureRegisteredExtension {

    private List<Object> _pendingImplementations;

    public TestContextExtension(Object... implementations) {
        addImplementations(implementations);
    }

    public void addImplementation(Object implementation) {
        addImplementations(implementation);
    }

    private void addImplementations(Object... implementations) {
        List<Object> implementationsList = Arrays.asList(implementations);
        if (isWithinTest()) {
            // Change the environment immediately.
            // Because isWithinTest() is true, this Extension must be registered correctly
            // and after() will be called.
            modifyTestContext(implementationsList);
        }
        else {
            // Store the changes and change the environment from before().
            // This ensures that after() will also be called to reinstate the environment.
            if (_pendingImplementations == null) {
                _pendingImplementations = new ArrayList<>(implementationsList);
            }
            else {
                _pendingImplementations.addAll(implementationsList);
            }
        }
    }

    // TODO! similar shenanigans to sys.prop.extension?
    @Override
    public void before(boolean isBeforeAll, ExtensionContext context) throws Exception {
        modifyTestContext(_pendingImplementations);
    }

    @Override
    public void after(boolean isWithinTest, ExtensionContext context) throws Exception {
        restoreTestContext();
    }

    private void modifyTestContext(List<Object> implementations) {
        //        TestContext testContext = TestContext.getInstance();
        //        for (Object implementation : implementations) {
        //            testContext.addImplementation(implementation);
        //        }
    }

    private void restoreTestContext() {
        // TestContext.getInstance().reset();
    }

}
