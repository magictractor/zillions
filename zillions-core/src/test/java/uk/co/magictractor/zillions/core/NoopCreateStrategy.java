package uk.co.magictractor.zillions.core;

import uk.co.magictractor.zillions.core.create.CreateStrategy;

public class NoopCreateStrategy implements CreateStrategy {

    // TODO! decimal is a bad name - fix everywhere
    @Override
    public BigInt fromString(String decimal) {
        return new NoopBigInt();
    }

    @Override
    public BigInt fromLong(long value) {
        return new NoopBigInt();
    }

}
