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
package uk.co.magictractor.zillions.biginteger.exporter;

import static uk.co.magictractor.zillions.core.bits.BitUtils.BYTE_00;
import static uk.co.magictractor.zillions.core.bits.BitUtils.BYTE_FF;

import java.util.Arrays;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.exporter.BigIntByteExporter;
import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.core.bits.BitUtils;

public class BigIntegerBigIntByteExporter implements BigIntByteExporter {

    @Override
    public void populateBytes(BigInt op, byte[] bytes) {
        int len = bytes.length;
        if (len == 0) {
            return;
        }
        if (len > 4) {
            // BigInteger doesn't provide a method which will populate a given byte array.
            byte[] allBytes = asBytes(op);
            if (allBytes.length >= bytes.length) {
                System.arraycopy(allBytes, allBytes.length - len, bytes, 0, len);
            }
            else {
                // bytes is larger than required, leading bytes are padded,
                // then bytes copied.
                byte padding = op.signum() >= 0 ? BYTE_00 : BYTE_FF;
                Arrays.fill(bytes, 0, len - allBytes.length, padding);
                System.arraycopy(allBytes, 0, bytes, len - allBytes.length, allBytes.length);
            }
            return;
        }

        // longValue() combines two intValue() calls, so just use array for that.
        int lowInt = BigIntegerBigInt.getInternalValue(op).intValue();
        BitUtils.setBytes(bytes, bytes.length - 4, lowInt);
    }

    @Override
    public byte[] asBytes(BigInt op) {
        return ((BigIntegerBigInt) op).getInternalValue().toByteArray();
    }

}
