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
package uk.co.magictractor.zillions.gmp;

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import com.google.common.base.MoreObjects;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.Init;
import uk.co.magictractor.zillions.api.random.RandomStrategy;
import uk.co.magictractor.zillions.gmp.struct.gmp_randstate_t;
import uk.co.magictractor.zillions.gmp.struct.mp_bitcnt_t;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

public class GmpRandomStrategy implements RandomStrategy, Init {

    private final gmp_randstate_t _state = new gmp_randstate_t();

    @Override
    public void init() {
        __lib.gmp_randinit_default(_state);
    }

    @Override
    public void setSeed(long seed) {
        __lib.gmp_randseed(_state, new GmpBigInt(seed).getInternalValue());
    }

    @Override
    public BigInt randomise(BigInt rop, int numBits) {
        mpz_t mpz = ((GmpBigInt) rop).getInternalValue();
        __lib.mpz_urandomb(mpz, _state, new mp_bitcnt_t(numBits));
        return rop;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

}
