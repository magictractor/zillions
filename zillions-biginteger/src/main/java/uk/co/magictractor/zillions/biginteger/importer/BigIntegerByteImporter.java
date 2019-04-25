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
package uk.co.magictractor.zillions.biginteger.importer;

import java.math.BigInteger;

import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.bits.BitUtils;
import uk.co.magictractor.zillions.core.importer.ByteImporter;

public class BigIntegerByteImporter implements ByteImporter {

    @Override
    public BigInt signedFrom(BigInt rop, byte[] bytes) {
        ((BigIntegerBigInt) rop).setInternalValue(new BigInteger(bytes));
        return rop;
    }

    @Override
    public BigInt unsignedFrom(BigInt rop, byte[] bytes) {
        BigInteger bigInteger = new BigInteger(bytes);
        if (BitUtils.isNegative(bytes)) {
            bigInteger = BigInteger.ONE.shiftLeft(bytes.length * 8).add(bigInteger);
        }
        ((BigIntegerBigInt) rop).setInternalValue(bigInteger);
        return rop;
    }

}
