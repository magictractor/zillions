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

import org.junit.jupiter.api.Assumptions;

public final class AssumeTestContext
{
  private AssumeTestContext() {
  }

  // TODO! we want a convenient way to run the test in isolation too. - bear in mind that impls will move to a sibling project.
  public static void assumeTextContext() {
    Assumptions.assumeTrue(TestContext.getInstance().hasImplementation(), "This test must have a BigIntCreateStrategy defined, which is usually done in a test suite which contains these tests");
  }

}
