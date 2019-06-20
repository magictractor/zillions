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
package uk.co.magictractor.zillions.junit.extension;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Extension which changes values, typically environment configuration, and
 * reverts the modified values when tests are complete.
 * </p>
 * <p>
 * Values are changed via {@link ValueChange} implementations, which ensures
 * that values are not changed if the extension is not registered properly.
 * Otherwise the extension would not receive callbacks after tests and so would
 * not be able to revert values
 * </p>
 * <p>
 * {@link ValueChange} implementations can also be used to revert values changed
 * by application code exercised by the test. To do this the
 * {@link ValueChange#apply()} implementation should just capture the current
 * value without modifying it.
 * </p>
 */
public abstract class AbstractValueChangeExtension
        implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback
{

    private final Logger _logger = LoggerFactory.getLogger(getClass());

    private boolean _isRegistered;
    private boolean _isWithinTest;

    // TODO! ThreadLocal or similar to detect if it hasn't been closed properly? maybe in finalize()?
    private ValueChanges _valueChanges = new ValueChanges();

    @Override
    public final void beforeAll(ExtensionContext context) throws Exception {
        System.err.println("beforeAll");
        applyPending(true);
        _isRegistered = true;
    }

    @Override
    public final void beforeEach(ExtensionContext context) throws Exception {
        System.err.println("beforeEach");

        if (!_isRegistered) {
            // When the extension is used in a non-static variable then beforeAll() will not be called.
            applyPending(false);
            _isRegistered = true;
        }
        else {
            // Reapply the value changes which would have been reverted after the previous test.
            // TODO! why didn't unit test fail before this was added?
            getValueChanges()._singleTest.forEach(ValueChange::apply);
        }

        _isWithinTest = true;
    }

    @Override
    public final void afterEach(ExtensionContext context) throws Exception {
        System.err.println("afterEach");
        _isWithinTest = false;
        revert(getValueChanges()._singleTest);
    }

    @Override
    public final void afterAll(ExtensionContext context) throws Exception {
        System.err.println("afterAll");
        revert(getValueChanges()._allTests);
    }

    private void applyPending(boolean isStatic) {
        ValueChanges valueChanges = getValueChanges();
        valueChanges._pending.forEach(ValueChange::apply);
        if (isStatic) {
            valueChanges._allTests = valueChanges._pending;
        }
        else {
            /*
             * There will be no beforeAll() and afterAll() callbacks, so value
             * changes must be applied and reverted for every test.
             */
            valueChanges._singleTest = valueChanges._pending;
        }
        valueChanges._pending = new ArrayList<>();
    }

    private void revert(List<ValueChange> valueChanges) {
        /*
         * Values reverted in reverse order in case the are multiple
         * ValueChanges modifying the same value.
         */
        Lists.reverse(valueChanges).forEach(ValueChange::revert);
        valueChanges.clear();
    }

    private ValueChanges getValueChanges() {
        return _valueChanges;
    }

    public void addValueChange(ValueChange valueChange) {
        ValueChanges valueChanges = getValueChanges();
        if (_isRegistered) {
            valueChange.apply();
            if (_isWithinTest) {
                valueChanges._singleTest.add(valueChange);
            }
            else {
                valueChanges._allTests.add(valueChange);
            }
        }
        else {
            /*
             * ValueChange.apply() is called later when the first callback is
             * received, in case the extension has not been registered properly.
             */
            valueChanges._pending.add(valueChange);
        }
    }

    protected Logger getLogger() {
        return _logger;
    }

    private static class ValueChanges {
        private List<ValueChange> _pending = new ArrayList<>();
        private List<ValueChange> _allTests = new ArrayList<>();
        private List<ValueChange> _singleTest = new ArrayList<>();
    }

}
