package uk.co.magictractor.zillions.testbed;

import java.util.function.BiFunction;

import uk.co.magictractor.zillions.core.BigInt;

public abstract class OpTestBigIntParam extends OpTestSingleParam<BigInt, BigInt> {

    protected OpTestBigIntParam(BiFunction<BigInt, BigInt, BigInt> op) {
        super(BigInt.class, BigInt.class, op);
    }

}
