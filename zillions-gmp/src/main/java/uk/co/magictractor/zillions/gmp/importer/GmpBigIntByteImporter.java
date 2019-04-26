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
package uk.co.magictractor.zillions.gmp.importer;

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import com.sun.jna.Memory;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.bits.BitUtils;
import uk.co.magictractor.zillions.core.importer.BigIntByteImporter;
import uk.co.magictractor.zillions.gmp.GmpBigInt;
import uk.co.magictractor.zillions.gmp.struct.mp_bitcnt_t;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

// TODO! can we avoid copying the bytes?
// https://stackoverflow.com/questions/5244214/get-pointer-of-byte-array-in-jna

public class GmpBigIntByteImporter implements BigIntByteImporter {

    @Override
    public BigInt signedFrom(BigInt rop, byte[] bytes) {
        unsignedFrom(rop, bytes);

        if (BitUtils.isNegative(bytes)) {
            GmpBigInt x = (GmpBigInt) rop;
            mpz_t mpz = x.getInternalValue();
            // Use the alternate value to prevent creating and throwing away an mpz_t.
            mpz_t alt = x.getAlternateInternalValue();
            __lib.mpz_set_si(alt, 1L);
            __lib.mpz_mul_2exp(alt, alt, new mp_bitcnt_t(bytes.length * 8));
            __lib.mpz_sub(mpz, mpz, alt);
        }

        return rop;
    }

    @Override
    public BigInt unsignedFrom(BigInt rop, byte[] bytes) {
        Memory memory = new Memory(bytes.length);
        memory.write(0, bytes, 0, bytes.length);

        mpz_t mpz = ((GmpBigInt) rop).getInternalValue();
        __lib.mpz_import(mpz, bytes.length, 1, 1, 1, 0, memory);

        return rop;
    }

}
