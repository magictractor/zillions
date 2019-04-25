package uk.co.magictractor.zillions.biginteger.bits;

import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.api.bits.BigIntBitLength;

public class BigIntegerBigIntBitLength implements BigIntBitLength {

    @Override
    public int bitLength(BigInt x) {
        return ((BigIntegerBigInt) x).getInternalValue().bitLength();
    }

}
