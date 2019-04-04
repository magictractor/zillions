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
package uk.co.magictractor.math.big.gmp.jna;

import uk.co.magictractor.math.big.BigInt;
import uk.co.magictractor.math.big.create.CreateStrategy;

public class GmpJnaCreateStrategy implements CreateStrategy
{

  private static GmpLib __lib;

  // private static final boolean IS_AVAILABLE;

  static {
    // TODO! make more robust
    // TODO! also make libraries optional (jna?)
    try {
      init();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private static void init() {
    __lib = new JnaGmpLib();
  }

  public BigInt fromString(String decimal) {
    return new GmpJnaBigInt(decimal, __lib);
  }

  public BigInt fromLong(long value) {
    return new GmpJnaBigInt(value, __lib);
  }

  public BigInt copy(BigInt other) {
    return new GmpJnaBigInt(other, __lib);
  }

  public boolean isAvailable() {
    return (__lib != null);
  }

}
