package uk.co.magictractor.zillions.testbed;

import java.util.function.BiFunction;

import uk.co.magictractor.zillions.core.BigInt;

public abstract class OpTestIntParam extends OpTestSingleParam<Integer, BigInt> {

    protected OpTestIntParam(BiFunction<BigInt, Integer, BigInt> op) {
        super(Integer.class, BigInt.class, op);
    }

}
