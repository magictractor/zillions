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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import uk.co.magictractor.zillions.core.environment.Init;

public class FilePropertyStrategy implements PropertyStrategy, Init
{

  private String _resource;
  private Properties _properties;

  public FilePropertyStrategy(String resource) {
    _resource = resource;
  }

  public void init()
    throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(_resource);
    _properties = new Properties();
    // TODO! indicate that the property is unavailable because the resource is missing
    if (inputStream != null) {
      _properties.load(inputStream);
    }
  }

  public boolean containsKey(String key) {
    return _properties.containsKey(key);
  }

  public String get(String key) {
    return _properties.getProperty(key);
  }

}
