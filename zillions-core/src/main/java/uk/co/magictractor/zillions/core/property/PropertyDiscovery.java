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
package uk.co.magictractor.zillions.core.property;

/** Properties are generally accessed via the methods on {@Link Environment}. */
public interface PropertyDiscovery {

    boolean containsKey(String key);

    String getString(String key);

    // TODO! change to Optional<> instead of using defaultValue
    default String getString(Class<?> keyClass, String keyField, String defaultValue) {
        return getString(key(keyClass, keyField), defaultValue);
    }

    default String getString(String key, String defaultValue) {
        String value = getString(key);
        return value == null ? defaultValue : value;
    }

    default boolean getBoolean(Class<?> keyClass, String keyField, boolean defaultValue) {
        return getBoolean(key(keyClass, keyField), defaultValue);
    }

    default boolean getBoolean(String key, boolean defaultValue) {
        String stringValue = getString(key);
        if (stringValue == null) {
            return defaultValue;
        }

        stringValue = stringValue.toLowerCase();
        boolean result;
        // TODO! other y/n values
        if (defaultValue) {
            result = "false".equals(stringValue);
        }
        else {
            result = "true".equals(stringValue);
        }

        return result;
    }

    default String key(Class<?> keyClass, String keyField) {
        return keyClass.getName() + "." + keyField;
    }

}
