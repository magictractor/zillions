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
package uk.co.magictractor.zillions.core.discovery;

import com.google.common.base.MoreObjects;

import uk.co.magictractor.zillions.core.environment.CachedStrategies;
import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.property.PropertyDecorator;
import uk.co.magictractor.zillions.core.property.PropertyStrategy;

/**
 * <p>Discovery strategy which finds interface implementations which are described in a
 * property read using a {@link PropertyStrategy}.</p>
 * 
 * <code>foo.bar.Interface.impl=foo.bar.Implementation</code>
 * 
 * <p>Multiple implementations may be defined for a interface by separating implementation
 * class names with commas.</p>
 * 
 * <code>foo.bar.Interface.impl=foo.bar.ImplementationOne,foo.bar.ImplementationTwo</code>
 */
public class ImplPropertyDiscoveryStrategy implements DiscoveryStrategy
{
	// TODO! this clunky decorator is used elsewhere too
  private static final PropertyDecorator PROPERTIES = new PropertyDecorator(Environment.getImplementations().getStrategyListWithoutDiscovery(PropertyStrategy.class));

  @Override
  public <T> CachedStrategies<T> discoverImplementations(Class<T> apiClass) {

    String impls = PROPERTIES.getString(apiClass, "impl", null);

    // TODO! allow this method to return null
    CachedStrategies<T> result = new CachedStrategies<T>(apiClass);
    if (impls != null) {
      String[] implArray = impls.split(",");
      for (String impl : implArray) {
        result.addStrategyClass(impl);
      }
    }

    return result;
  }

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).toString();
	}
}
