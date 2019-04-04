/**
 * Copyright 2015 Ken Dobson
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

import uk.co.magictractor.zillions.core.environment.Strategies;

public class PropertyDecorator
{
  private Strategies<PropertyStrategy> _propertyStrategies;

  public PropertyDecorator(Strategies<PropertyStrategy> propertyStrategies) {
    _propertyStrategies = propertyStrategies;
  }

  public boolean containsKey(String key) {
    return (findPropertyStrategyContainingKey(key) != null);
  }

  private PropertyStrategy findPropertyStrategyContainingKey(String key) {
    for (PropertyStrategy candidate : _propertyStrategies.allAvailable()) {
      if (candidate.containsKey(key)) {
        return candidate;
      }
    }
    return null;
  }

  public String getString(String key) {
    PropertyStrategy delegate = findPropertyStrategyContainingKey(key);
    return delegate == null ? null : delegate.get(key);
  }

  public String getString(Class<?> keyClass, String keyField, String defaultValue) {
    return getString(key(keyClass, keyField), defaultValue);
  }

  public String getString(String key, String defaultValue) {
    String value = getString(key);
    return value == null ? defaultValue : value;
  }

  public boolean getBoolean(Class<?> keyClass, String keyField, boolean defaultValue) {
    return getBoolean(key(keyClass, keyField), defaultValue);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    String stringValue = getString(key);
    if (stringValue == null) {
      return defaultValue;
    }

    stringValue = stringValue.toLowerCase();
    boolean result;
    // TODO! other y/n values
    if (defaultValue) {
      result = "false".equals(stringValue);
    } else {
      result = "true".equals(stringValue);
    }

    return result;
  }

  private String key(Class<?> keyClass, String keyField) {
    return keyClass.getName() + "." + keyField;
  }

}
