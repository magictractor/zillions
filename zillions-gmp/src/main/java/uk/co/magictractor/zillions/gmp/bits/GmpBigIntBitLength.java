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
