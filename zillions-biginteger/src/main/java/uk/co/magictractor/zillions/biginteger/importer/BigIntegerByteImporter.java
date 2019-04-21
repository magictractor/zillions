package uk.co.magictractor.zillions.biginteger.importer;

import java.math.BigInteger;

import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.importer.ByteImporter;

public class BigIntegerByteImporter implements ByteImporter {

	@Override
	public BigInt from(BigInt rop, byte[] bytes) {
		((BigIntegerBigInt) rop).setInternalValue(new BigInteger(bytes));
		return rop;
	}

}
