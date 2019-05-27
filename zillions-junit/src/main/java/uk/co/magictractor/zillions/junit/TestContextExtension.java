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

import java.util.List;

import uk.co.magictractor.zillions.junit.extension.AbstractValueChangeExtension;
import uk.co.magictractor.zillions.junit.extension.CollectionAddValueChange;
import uk.co.magictractor.zillions.junit.extension.SetterValueChange;
import uk.co.magictractor.zillions.junit.extension.ValueChange;

public class TestContextExtension extends AbstractValueChangeExtension {

    public TestContextExtension(Object... implementations) {
        addImplementations(implementations);
    }

    public void addImplementation(Object implementation) {
        addImplementations(implementation);
    }

    private void addImplementations(Object... implementations) {
        List<Object> testContextImpls = TestContext.getInstance().getTestImplementations();
        for (Object implementation : implementations) {
            addValueChange(new CollectionAddValueChange<>(testContextImpls, implementation));
        }
    }

    public TestContextExtension disableProxies() {
        System.err.println("disabledProxies");
        TestContext context = TestContext.getInstance();
        ValueChange implProxyValueChange = new SetterValueChange<>(context::isImplementationProxyEnabled,
            context::setImplementationProxyEnabled, false);
        addValueChange(implProxyValueChange);
        return this;
    }

}
