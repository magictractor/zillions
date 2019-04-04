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
package uk.co.magictractor.zillions.core.junit;

import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemProperties extends ExternalResource
{
  private final Logger _logger = LoggerFactory.getLogger(getClass());

  /**
   * Properties are backed by a Hashtable, which does not permit null values. Null values
   * here indicate that a new property was added to the system properties.
   */
  private Map<String, String> _originalProperties = new HashMap<String, String>();

  public void clearProperty(String key) {
    setProperty(key, null);
  }

  public void setProperty(String key, String value) {
    String originalValue;
    if (value == null) {
      originalValue = System.clearProperty(key);
      _logger.debug("System property '{}' cleared", key);
    } else {
      originalValue = System.setProperty(key, value);
      _logger.debug("System property '{}' set to '{}'", key, value);
    }

    // Guard against one test changing a property more than once.
    if (!_originalProperties.containsKey(key)) {
      _originalProperties.put(key, originalValue);
    }
  }

  @Override
  protected void after() {
    for (Map.Entry<String, String> entry : _originalProperties.entrySet()) {
      String value = entry.getValue();
      if (value == null) {
        System.clearProperty(entry.getKey());
      } else {
        System.setProperty(entry.getKey(), value);
      }
    }
  }

}
