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

import org.junit.rules.ExternalResource;

public class TestContextRule extends ExternalResource
{

  public TestContextRule(Object... implementations) {
    addImplementations(implementations);
  }

  @Override
  protected void after() {
    TestContext.getInstance().reset();
  }

  public void addImplementations(Object... implementations) {
    for (Object implementation : implementations) {
      addImplementation(implementation);
    }
  }

  public void addImplementation(Object implementation) {
    TestContext.getInstance().addImplementation(implementation);
  }

}
