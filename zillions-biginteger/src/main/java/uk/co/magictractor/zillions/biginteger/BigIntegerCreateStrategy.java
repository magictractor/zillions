/**
 * Copyright 2015-2019 Ken Dobson
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
package uk.co.magictractor.zillions.biginteger;

import com.google.common.base.MoreObjects;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.create.CreateStrategy;

public class BigIntegerCreateStrategy implements CreateStrategy {

    @Override
    public BigInt fromString(String decimal) {
        return new BigIntegerBigInt(decimal);
    }

    public boolean isAvailable() {
        return true;
    }

    @Override
    public BigInt fromLong(long value) {
        return new BigIntegerBigInt(value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

}
