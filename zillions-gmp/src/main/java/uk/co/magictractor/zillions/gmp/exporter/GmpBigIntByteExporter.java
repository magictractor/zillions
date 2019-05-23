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

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.bits.BigIntBitLength;
import uk.co.magictractor.zillions.api.exporter.BigIntByteExporter;
import uk.co.magictractor.zillions.core.bits.BitUtils;
import uk.co.magictractor.zillions.environment.Environment;
import uk.co.magictractor.zillions.gmp.GmpBigInt;
import uk.co.magictractor.zillions.gmp.struct.mp_bitcnt_t;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

/**
 * GMP byte exporter. This currently works by copying bytes from the least
 * significant integer and then bit shifting, so is not efficient for very large
 * numbers.
 */
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

    @Override
    public void populateBytes(BigInt op, byte[] bytes) {

        int intCount = (bytes.length + 3) / 4;

        mpz_t mpz = GmpBigInt.getInternalValue(op);
        boolean isNegative = mpz._mp_size < 0;
        if (isNegative) {
            // GMP imports and exports only unsigned values.
            mpz_t alt = GmpBigInt.getAltInternalValue(op);
            __lib.mpz_com(alt, mpz);
            mpz = alt;
        }
        else if (intCount > 1) {
            // Bits will be shifted, so use a copy.
            mpz_t alt = GmpBigInt.getAltInternalValue(op);
            __lib.mpz_set(alt, mpz);
            mpz = alt;
        }

        int index = bytes.length;
        for (int i = 1; i <= intCount && mpz._mp_size != 0; i++) {

            if (i > 1) {
                // shift right
                __lib.mpz_fdiv_q_2exp(mpz, mpz, new mp_bitcnt_t(32));
            }

            int lowInt = __lib.mpz_get_ui(mpz);
            if (isNegative) {
                /*
                 * Bit flip rather than the whole number subtraction done for
                 * import because this could be a single byte from a very large
                 * number.
                 */
                lowInt = lowInt ^ 0xffffffff;
            }

            index -= 4;
            BitUtils.setBytes(bytes, index, lowInt);
        }

        // Fill any bytes which were not copied with correct padding.
        if (index > 0) {
            byte padding = isNegative ? BitUtils.BYTE_FF : BitUtils.BYTE_00;
            Arrays.fill(bytes, 0, index, padding);
        }
    }

    @Override
    public byte[] asBytes(BigInt op) {
        int bitLength = Environment.findImplementation(BigIntBitLength.class).bitLength(op);
        byte[] bytes = new byte[(bitLength + 8) / 8];
        populateBytes(op, bytes);

        return bytes;
    }

}
