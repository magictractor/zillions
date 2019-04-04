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
package uk.co.magictractor.math.big.discovery;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import uk.co.magictractor.math.environment.CachedStrategies;

public class SpiDiscoveryStrategy implements DiscoveryStrategy
{

  @Override
  public <T> CachedStrategies<T> discoverImplementations(Class<T> apiClass) {
    ServiceLoader<T> serviceLoader = ServiceLoader.load(apiClass);
    CachedStrategies<T> implementations = new CachedStrategies<T>(apiClass);
    // TODO! handle exceptions and mark the failed service as unavailable
    // for (T implementation : serviceLoader) {
    // implementations.addStrategyImplementation(implementation);
    // }
    Iterator<T> serviceIterator = serviceLoader.iterator();
    while (serviceIterator.hasNext()) {
      try {
        T strategy = serviceIterator.next();
        implementations.addStrategyImplementation(strategy);
        // TODO! subsequent strategies won't be loaded
      } catch (ServiceConfigurationError e) {
        implementations.addStrategyUnavailable(e);
      }
    }
    return implementations;
  }

}
