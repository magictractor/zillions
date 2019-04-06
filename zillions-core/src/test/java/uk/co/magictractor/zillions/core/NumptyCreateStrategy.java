package uk.co.magictractor.zillions.core;

import uk.co.magictractor.zillions.core.create.CreateStrategy;

public class NumptyCreateStrategy implements CreateStrategy {

	@Override
	public boolean isAvailable() {
		return true;
	}

	// TODO! decimal is a bad name - fix everywhere
	@Override
	public BigInt fromString(String decimal) {
		return fromLong(Long.parseLong(decimal));
	}

	@Override
	public BigInt fromLong(long value) {
		return new NumptyBigInt(value);
	}

	@Override
	public BigInt copy(BigInt other) {
		throw new UnsupportedOperationException("not yet implemented");
	}

}
