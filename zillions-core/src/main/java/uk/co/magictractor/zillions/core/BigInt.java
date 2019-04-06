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
package uk.co.magictractor.zillions.core;

/**
 * <p> BigInt implementations are mutable. All methods should return "this" to allow daisy
 * chaining of operations.</p>
 * 
 * <p> As well as the methods defined on this interface, implementations will require
 * implementations of toString(), equals() and hashCode().</p>
 */
public interface BigInt
{

  BigInt add(BigInt y);

  BigInt add(long y);

  BigInt subtract(BigInt y);

  BigInt subtract(long y);

  BigInt multiply(BigInt y);

  BigInt multiply(long y);

  BigInt or(BigInt y);
  
  BigInt and(BigInt y);
  
  BigInt xor(BigInt y);

}
