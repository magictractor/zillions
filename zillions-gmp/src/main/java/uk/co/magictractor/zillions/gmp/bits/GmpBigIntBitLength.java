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
package uk.co.magictractor.zillions.gmp.bits;

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.api.bits.BigIntBitLength;
import uk.co.magictractor.zillions.gmp.GmpBigInt;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

public class GmpBigIntBitLength implements BigIntBitLength {

    @Override
    public int bitLength(BigInt x) {
        mpz_t mpz = ((GmpBigInt) x).getInternalValue();

        // x < 0
        if (mpz._mp_size < 0) {
            mpz_t alt = ((GmpBigInt) x).getAlternateInternalValue();
            __lib.mpz_com(alt, mpz);
            mpz = alt;
        }

        // x == 0 or -1
        if (mpz._mp_size == 0) {
            // mpz_sizeinbase would say 1
            return 0;
        }

        return __lib.mpz_sizeinbase(mpz, 2);
    }

}
