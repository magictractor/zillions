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
package uk.co.magictractor.math.big.biginteger;

import java.math.BigInteger;

import uk.co.magictractor.math.big.BigInt;

public class BigIntegerBigInt implements BigInt
{

  private BigInteger _bigInteger;

  public BigIntegerBigInt(String decimal) {
    _bigInteger = new BigInteger(decimal);
  }

  public BigIntegerBigInt(long value) {
    _bigInteger = BigInteger.valueOf(value);
  }

  public BigIntegerBigInt(BigInt other) {
    _bigInteger = ((BigIntegerBigInt) other)._bigInteger;
  }

  public BigInt add(BigInt y) {
    _bigInteger = _bigInteger.add(((BigIntegerBigInt) y)._bigInteger);
    return this;
  }

  public BigInt add(long y) {
    _bigInteger = _bigInteger.add(BigInteger.valueOf(y));
    return this;
  }

  public BigInt subtract(BigInt y) {
    _bigInteger = _bigInteger.subtract(((BigIntegerBigInt) y)._bigInteger);
    return this;
  }

  public BigInt subtract(long y) {
    _bigInteger = _bigInteger.subtract(BigInteger.valueOf(y));
    return this;
  }

  public BigInt multiply(BigInt y) {
    _bigInteger = _bigInteger.multiply(((BigIntegerBigInt) y)._bigInteger);
    return this;
  }

  public BigInt multiply(long y) {
    _bigInteger = _bigInteger.multiply(BigInteger.valueOf(y));
    return this;
  }

  public BigInt xor(BigInt y) {
    _bigInteger = _bigInteger.xor(((BigIntegerBigInt) y)._bigInteger);
    return this;
  }

  public String toString() {
    return _bigInteger.toString();
  }

  public boolean equals(Object other) {
    boolean equals = false;
    if (other != null && other.getClass() == BigIntegerBigInt.class) {
      equals = _bigInteger.equals(((BigIntegerBigInt) other)._bigInteger);
    }
    return equals;
  }

  public int hashCode() {
    return _bigInteger.hashCode();
  }

}
