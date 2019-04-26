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

import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.exporter.BigIntByteExporter;

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
            if (allBytes.length > bytes.length) {
                // System.arraycopy(src, srcPos, dest, destPos, length);
            }
            else {
                // bytes is larger than required, leading bytes are set to zero,
                // then bytes copied.
            }
            return;
        }

        // longValue() combines two intValue() calls, so just use array for that.
        int lowInt = BigIntegerBigInt.getInternalValue(op).intValue();
        int index = 0;
        if (len == 4) {
            bytes[index++] = (byte) (lowInt >> 24);
        }
        if (len >= 3) {
            bytes[index++] = (byte) (lowInt >> 16);
        }
        if (len >= 2) {
            bytes[index++] = (byte) (lowInt >> 8);
        }
        bytes[index] = (byte) lowInt;
    }

    @Override
    public byte[] asBytes(BigInt op) {
        return ((BigIntegerBigInt) op).getInternalValue().toByteArray();
    }

}
