package uk.co.magictractor.zillions.gmp.bits;

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.api.bits.BigIntBitCount;
import uk.co.magictractor.zillions.gmp.GmpBigInt;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

public class GmpBigIntBitCount implements BigIntBitCount {

	@Override
	public int bitCount(BigInt x) {
		mpz_t mpz = ((GmpBigInt) x).getInternalValue();
		if (mpz._mp_size < 0) {
			mpz_t alt = ((GmpBigInt) x).getAlternateInternalValue();
			__lib.mpz_com(alt, mpz);
			mpz = alt;
		}
		return __lib.mpz_popcount(mpz).intValue();
	}

}
