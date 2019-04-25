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
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemPropertiesExtension extends EnsureRegisteredExtension {

    private final Logger _logger = LoggerFactory.getLogger(getClass());

    // Property values set in field declarations, or @BeforeAll.
    private List<PropertyValue> _fieldPropertyValues = new ArrayList<>();
    // Property values set within test methods.
    private List<PropertyValue> _methodPropertyValues = new ArrayList<>();

    public SystemPropertiesExtension withoutProperty(String key) {
        return withProperty(key, null);
    }

    public SystemPropertiesExtension withProperty(Class<?> apiClass, String apiClassPropertyName, String value) {
        return withProperty(apiClass.getName() + "." + apiClassPropertyName, value);
    }

    // TODO! do not change environment immediately (see super)
    public SystemPropertiesExtension withProperty(String propertyName, String value) {
        ensureRegistered();

        PropertyValue propertyValue;

        List<PropertyValue> propertyValues = isWithinTest() ? _methodPropertyValues : _fieldPropertyValues;

        // Guard against one test changing a property more than once.
        // Assume a very small number of modified property values, so iterating over the List is sufficiently fast (likely faster than a HashMap).
        Optional<PropertyValue> existing = propertyValues.stream().filter(
            pv -> pv._propertyName.equals(propertyName)).findAny();
        if (existing.isPresent()) {
            propertyValue = existing.get();
            propertyValue._temporaryValue = value;
        }
        else {
            propertyValue = new PropertyValue(propertyName, value);
            propertyValues.add(propertyValue);
        }

        // isRegistered() to handle values set in test @BeforeAll which happens after Extension beforeAll().
        if (isRegistered()) {
            propertyValue.setTemporaryValue();
        }

        return this;
    }

    @Override
    public void before(boolean isBeforeAll, ExtensionContext context) throws Exception {
        if (isBeforeAll || !isStatic()) {
            _fieldPropertyValues.forEach(PropertyValue::setTemporaryValue);
        }
    }

    @Override
    public void after(boolean isAfterAll, ExtensionContext context) throws Exception {
        //        Iterator<PropertyValue> propertyValueIterator = _propertyValues.iterator();
        //        while (propertyValueIterator.hasNext()) {
        //            PropertyValue propertyValue = propertyValueIterator.next();
        //            // TODO! check and return
        //
        //            propertyValue.restoreOriginalValue();
        //            propertyValueIterator.remove();
        //        }

        if (isAfterAll) {
            // _methodPropertyValues should already have been restored
            restoreOriginalValues(_fieldPropertyValues);
        }
        else {
            restoreOriginalValues(_methodPropertyValues);
            _methodPropertyValues.clear();
            if (!isStatic()) {
                // Not static, so we don't know if this is the last test.
                // All properties need to be restored, and set again if there's another test.
                restoreOriginalValues(_fieldPropertyValues);
            }
        }
    }

    private void restoreOriginalValues(List<PropertyValue> propertyValues) {
        propertyValues.forEach(PropertyValue::restoreOriginalValue);
        //propertyValues.clear();
    }

    // TODO! scope for temporary values? Could have some for all tests, others
    public static class PropertyValue {
        private final String _propertyName;
        private final String _originalValue;
        private String _temporaryValue;

        public PropertyValue(String propertyName, String temporaryValue) {
            _propertyName = propertyName;
            _originalValue = System.getProperty(propertyName);
            _temporaryValue = temporaryValue;
        }

        public void setTemporaryValue() {
            setPropertyValue(_temporaryValue);
            System.err.println("set temporary value: " + _propertyName + " -> " + _temporaryValue);
        }

        public void restoreOriginalValue() {
            setPropertyValue(_originalValue);
            System.err.println("restored original value: " + _propertyName + " -> " + _originalValue);
        }

        private void setPropertyValue(String value) {
            if (value == null) {
                System.clearProperty(_propertyName);
            }
            else {
                System.setProperty(_propertyName, value);
            }
        }
    }

}
