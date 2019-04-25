package uk.co.magictractor.zillions.biginteger.importer;

import java.math.BigInteger;

import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.bits.BitUtils;
import uk.co.magictractor.zillions.core.importer.ByteImporter;

public class BigIntegerByteImporter implements ByteImporter {

    @Override
    public BigInt signedFrom(BigInt rop, byte[] bytes) {
        ((BigIntegerBigInt) rop).setInternalValue(new BigInteger(bytes));
        return rop;
    }

    @Override
    public BigInt unsignedFrom(BigInt rop, byte[] bytes) {
        BigInteger bigInteger = new BigInteger(bytes);
        if (BitUtils.isNegative(bytes)) {
            bigInteger = BigInteger.ONE.shiftLeft(bytes.length * 8).add(bigInteger);
        }
        ((BigIntegerBigInt) rop).setInternalValue(bigInteger);
        return rop;
    }

}
