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
package uk.co.magictractor.zillions.gmp.exporter;

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import java.util.Arrays;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.api.bits.BigIntBitLength;
import uk.co.magictractor.zillions.core.bits.BitUtils;
import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.exporter.BigIntByteExporter;
import uk.co.magictractor.zillions.gmp.GmpBigInt;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

public class GmpBigIntByteExporter implements BigIntByteExporter {

    // From https://gmplib.org/manual/Integer-Import-and-Export.html#Integer-Import-and-Export
    //    numb = 8*size - nail;
    //    count = (mpz_sizeinbase (z, 2) + numb-1) / numb;
    //    p = malloc (count * size);
    //
    // size is 1 (words are single byte) and nail is zero
    // numb = 8*1 - 0 = 8
    // count = (mpz_sizeinbase (z, 2) + 7) / 8
    // p = malloc (count)

    // Ah! how about using unsigned long int mpz_get_ui (const mpz_t op)

    @Override
    public void populateBytes(BigInt op, byte[] bytes) {
        //        Memory memory = new Memory(bytes.length);
        //        IntByReference writtenRef = new IntByReference();
        //        mpz_t mpz = GmpBigInt.getInternalValue(op);
        //
        //        // TODO! this can write more byte than are in the buffer
        //        GmpLibInstance.__lib.mpz_export(memory, writtenRef, 1, 1, 1, 0, mpz);
        //
        //        int written = writtenRef.getValue();
        //        System.err.println("written: " + written);
        //        if ()

        boolean isCopy;

        mpz_t mpz = GmpBigInt.getInternalValue(op);
        boolean isNegative = mpz._mp_size < 0;
        if (isNegative) {
            // GMP imports and exports only unsigned values.
            mpz_t alt = GmpBigInt.getAltInternalValue(op);
            //__lib.mpz_com(alt, mpz);
            __lib.mpz_add_ui(alt, mpz, 1L);
            mpz = alt;
            isCopy = true;
        }

        // TODO! fill only unpopulated bytes at the end
        //        byte padding = isNegative ? BYTE_FF : BYTE_00;
        Arrays.fill(bytes, BitUtils.BYTE_00);

        //int from 
        //   while (mpz._mp_size != 0) {
        int lowInt = __lib.mpz_get_ui(mpz);
        int len = Math.min(4, bytes.length);
        int index = bytes.length - len;
        // TODO! util for this? see also BigInteger exported
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
        //        }

        if (isNegative) {
            // Flip rather than subtraction done for import because this could be a single byte from a very large number.
            BitUtils.flipBytes(bytes);
        }
    }

    @Override
    public byte[] asBytes(BigInt op) {
        //mpz_t mpz = GmpBigInt.getInternalValue(op);
        //size = mpz.
        int bitLength = Environment.getBestAvailableImplementation(BigIntBitLength.class).bitLength(op);
        // Ah! bitCount results are rubbish
        System.err.println("bitLength: " + bitLength + " for " + op);
        // TODO! might need another byte for negative numbers?
        // 0->1 7->1 8->2
        byte[] bytes = new byte[(bitLength + 8) / 8];
        populateBytes(op, bytes);

        return bytes;
    }

}
