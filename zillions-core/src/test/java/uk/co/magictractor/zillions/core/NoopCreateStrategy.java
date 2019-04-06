package uk.co.magictractor.zillions.core;

import uk.co.magictractor.zillions.core.create.CreateStrategy;

public class NoopCreateStrategy implements CreateStrategy {

	@Override
	public boolean isAvailable() {
		return true;
	}

	// TODO! decimal is a bad name - fix everywhere
	@Override
	public BigInt fromString(String decimal) {
		return new NoopBigInt();
	}

	@Override
	public BigInt fromLong(long value) {
		return new NoopBigInt();
	}

	@Override
	public BigInt copy(BigInt other) {
		throw new UnsupportedOperationException("not yet implemented");
	}

}
