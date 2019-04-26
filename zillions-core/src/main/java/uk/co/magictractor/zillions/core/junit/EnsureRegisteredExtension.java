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

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 *
 * Generally, setters should not change the environment immediately, unless
 * {@link #isWithinTest()} is true. Instead setters should store state, and the
 * {@link #before} implementation should change the environment. Doing so guards
 * against incorrect use of the Extension (missing annotation, private field
 * etc), causing a test to fail because the environment has not been changed,
 * rather than subsequent tests failing because {@link #after} was not called
 * and the change made for the test remained in the environment.
 */
// TODO! wrong extension used?
// See https://junit.org/junit5/docs/current/user-guide/#extensions-lifecycle-callbacks
// I thought I must have missed something!!
public abstract class EnsureRegisteredExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback,
        AfterEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback
{

    private boolean _isRegistered;
    // TODO! is there something in ExtensionContext which indicates whether the
    // registered extension is static?
    private boolean _isStatic;
    private boolean _isWithinTest;

    // can this be removed?
    protected boolean isWithinTest() {
        return _isWithinTest;
    }

    protected boolean isRegistered() {
        return _isRegistered;
    }

    protected boolean isStatic() {
        return _isStatic;
    }

    @Override
    public final void beforeAll(ExtensionContext context) throws Exception {
        System.err.println("beforeAll");
        _isStatic = true;
        _isRegistered = true;
        before(true, context);
    }

    @Override
    public final void beforeEach(ExtensionContext context) throws Exception {
        System.err.println("beforeEach");
        _isRegistered = true;
        _isWithinTest = true;
        // if (!all) {
        before(false, context);
        // }
    }

    protected void ensureRegistered() {
        if (_isWithinTest && !_isRegistered) {
            throw new IllegalStateException(
                "Extension is not registered, so will be torn down properly, and could cause later tests to fail.");
        }
    }

    @Override
    public final void afterEach(ExtensionContext context) throws Exception {
        System.err.println("afterEach");
        after(false, context);
        _isWithinTest = false;
    }

    @Override
    public final void afterAll(ExtensionContext context) throws Exception {
        System.err.println("afterAll");
        after(true, context);
    }

    public abstract void before(boolean isBeforeAll, ExtensionContext context) throws Exception;

    public abstract void after(boolean isAfterAll, ExtensionContext context) throws Exception;

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.err.println("beforeTestExecution");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        System.err.println("afterTestExecution");
    }

}
