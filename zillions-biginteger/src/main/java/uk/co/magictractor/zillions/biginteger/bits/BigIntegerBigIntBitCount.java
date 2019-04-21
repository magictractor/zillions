package uk.co.magictractor.zillions.biginteger.bits;

import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.api.bits.BigIntBitCount;

public class BigIntegerBigIntBitCount implements BigIntBitCount {

	@Override
	public int bitCount(BigInt x) {
		return ((BigIntegerBigInt) x).getInternalValue().bitCount();
	}

}
