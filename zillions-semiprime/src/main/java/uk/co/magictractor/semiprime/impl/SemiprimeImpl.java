package uk.co.magictractor.semiprime.impl;

import uk.co.magictractor.semiprime.BigIntPair;
import uk.co.magictractor.semiprime.api.Semiprime;
import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.environment.BigIntFactory;

public class SemiprimeImpl implements Semiprime {

    @Override
    public BigIntPair calculateFactors(BigInt semiprime) {
        if (semiprime.testBit(0)) {
            // Unusual case, an even number, so 2 is a factor.
            return evenFactor(semiprime);
        }

        return null;
    }

    private BigIntPair evenFactor(BigInt n) {
        BigInt left = BigIntFactory.from(2);
        BigInt right = n.copy().shiftRight(1);
        return BigIntPair.ordered(left, right);
    }

}
