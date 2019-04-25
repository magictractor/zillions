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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;

@ExtendWith(SystemPropertiesExtensionTest.PostTestsCheck.class)
public class SystemPropertiesExtensionTest {

    private static final String STATIC_KEY_FIELD = "__unitTest__static_field";
    private static final String STATIC_KEY_BEFORE_ALL = "__unitTest__static_before_all";
    private static final String STATIC_KEY_BEFORE_EACH = "__unitTest__static_before_each";
    private static final String INSTANCE_KEY_FIELD = "__unitTest__instance_field";
    private static final String INSTANCE_KEY_BEFORE_EACH = "__unitTest__instance_before_each";
    private static final String INITIAL_VALUE = "999";

    @RegisterExtension
    public static SystemPropertiesExtension STATIC_EXTENSION = new SystemPropertiesExtension().withProperty(
        STATIC_KEY_FIELD, INITIAL_VALUE);

    @RegisterExtension
    public SystemPropertiesExtension _instanceExtension = new SystemPropertiesExtension().withProperty(
        INSTANCE_KEY_FIELD, INITIAL_VALUE);

    @BeforeAll
    public static void beforeAll() {
        // Note that extension beforeAll() has already been called at this point.
        STATIC_EXTENSION.withProperty(STATIC_KEY_BEFORE_ALL, INITIAL_VALUE);
    }

    @BeforeEach
    public void beforeEach() {
        STATIC_EXTENSION.withProperty(STATIC_KEY_BEFORE_EACH, INITIAL_VALUE);
        _instanceExtension.withProperty(INSTANCE_KEY_BEFORE_EACH, INITIAL_VALUE);
    }

    @Test
    public void test1() {
        checkInitialSystemPropertyValues();

        setExtensionValues("aaa");

        checkSystemPropertyValues("aaa");
    }

    // Looks like a duplicate of test1, but the important bit is that the initial
    // values haven't been mucked up by a previous test.
    @Test
    public void test2() {
        checkInitialSystemPropertyValues();

        setExtensionValues("ppp");

        checkSystemPropertyValues("ppp");
    }

    private void setExtensionValues(String value) {
        STATIC_EXTENSION.withProperty(STATIC_KEY_FIELD, value);
        STATIC_EXTENSION.withProperty(STATIC_KEY_BEFORE_ALL, value);
        STATIC_EXTENSION.withProperty(STATIC_KEY_BEFORE_EACH, value);
        _instanceExtension.withProperty(INSTANCE_KEY_FIELD, value);
        _instanceExtension.withProperty(INSTANCE_KEY_BEFORE_EACH, value);
    }

    private static void checkInitialSystemPropertyValues() {
        checkSystemPropertyValues(INITIAL_VALUE);
    }

    private static void checkSystemPropertyValues(String expectedValue) {
        checkSystemPropertyValues(expectedValue, expectedValue);
    }

    private static void checkSystemPropertyValues(String classScopedValue, String methodScopedValue) {
        // Perhaps use assertAll once formatting rules have been configured
        // Assertions.assertAll(() ->
        // assertThat(System.getProperty(STATIC_KEY)).isEqualTo(expectedValue),
        // () -> assertThat(System.getProperty(INSTANCE_KEY)).isEqualTo(expectedValue));

        assertThat(System.getProperty(STATIC_KEY_FIELD)).isEqualTo(classScopedValue);
        assertThat(System.getProperty(STATIC_KEY_BEFORE_ALL)).isEqualTo(classScopedValue);
        // A static field, but the value was set just for the scope of the method in @BeforeEach.
        assertThat(System.getProperty(STATIC_KEY_BEFORE_EACH)).isEqualTo(methodScopedValue);
        assertThat(System.getProperty(INSTANCE_KEY_FIELD)).isEqualTo(methodScopedValue);
        assertThat(System.getProperty(INSTANCE_KEY_BEFORE_EACH)).isEqualTo(methodScopedValue);
    }

    public static class PostTestsCheck implements BeforeAllCallback, AfterEachCallback, AfterAllCallback {

        @Override
        public void beforeAll(ExtensionContext context) {
            System.err.println("PostTestsCheck.beforeAll");
        }

        @Override
        public void afterEach(ExtensionContext context) {
            System.err.println("STATIC_KEY_FIELD: " + System.getProperty(STATIC_KEY_FIELD));
            System.err.println("STATIC_KEY_BEFORE_ALL: " + System.getProperty(STATIC_KEY_BEFORE_ALL));
            System.err.println("STATIC_KEY_BEFORE_EACH: " + System.getProperty(STATIC_KEY_BEFORE_EACH));
            System.err.println("INSTANCE_KEY_FIELD: " + System.getProperty(INSTANCE_KEY_FIELD));
            System.err.println("INSTANCE_KEY_BEFORE_EACH: " + System.getProperty(INSTANCE_KEY_BEFORE_EACH));

            // Check that static scoped values remain, and method scoped values have been restored.
            checkSystemPropertyValues(INITIAL_VALUE, null);
        }

        @Override
        public void afterAll(ExtensionContext context) {
            // Check that all properties have been restored.
            checkSystemPropertyValues(null);
        }

    }

}
