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
package uk.co.magictractor.zillions.core.create;

import uk.co.magictractor.zillions.core.BigInt;

/**
 * <p> Strategy for the creation of BigInts. </p> <p> Implementations will be associated
 * with a Java or native library for implementing large integers, such as
 * java.lang.BigInteger or GNU's gmp library. <p>
 */
public interface CreateStrategy
{

  /**
   * <p> A typical reason for not being available is that a required native library is not
   * available. </p> <p> If this method returns false it is recommended that information
   * is logged to indicate the reason. The BigInt framework should only call this method
   * once, avoiding noise in logs. </p>
   * 
   * @return true if is this strategy is available for use; false otherwise
   */
  boolean isAvailable();

  BigInt fromString(String decimal);

  BigInt fromLong(long value);

  BigInt copy(BigInt other);

}