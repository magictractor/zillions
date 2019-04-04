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
package uk.co.magictractor.zillions.test.common;

import org.junit.runner.Description;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class ReuseSuite extends Suite
{
  public static String __name;

  public ReuseSuite(Class<?> klass, RunnerBuilder builder)
    throws InitializationError {
    super(klass, builder);
  }

  /*
  @Override
  public void run(final RunNotifier notifier) {
    __name = getClass().getSimpleName();
    super.run(notifier);
  }*/

  /*
  @Override
  protected boolean isIgnored(Runner child) {
    return !TestContext.getInstance().hasImplementation();
  }
  */

  @Override
  public Description getDescription() {
    boolean isAlreadyNamed = (__name != null);
    if (!isAlreadyNamed) {
      __name = getNameForRunner();
    }
    Description description = super.getDescription();
    if (!isAlreadyNamed) {
      __name = null;
    }
    return description;
  }

  private String getNameForRunner() {
    String name = getName();
    name = name.substring(name.lastIndexOf(".") + 1);
    if (name.endsWith("Suite")) {
      name = name.substring(0, name.length() - 5);
    }
    return name;
  }

  /*
  @Override
  protected Description describeChild(Runner child) {
    Description desc = child.getDescription();
    String expandedName = desc.getDisplayName() + "(" + getClass().getSimpleName() + ")";
    return Description.createSuiteDescription(expandedName);
  }
  */

}
