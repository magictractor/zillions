package uk.co.magictractor.zillions.testbed.random;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.magictractor.zillions.core.BigIntFactory.from;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.create.RandomStrategy;
import uk.co.magictractor.zillions.core.environment.Environment;

public class RandomTest {

	@Test
	public void testRandom() {
		// Lots of bits, so the chances of an intermittent failure are extremely
		// small.
		BigInt random1 = BigIntFactory.random(1024);
		BigInt random2 = BigIntFactory.random(1024);

		assertThat(random1).isNotEqualTo(random2);
	}

	@Test
	public void testRandomSeed() {
		RandomStrategy randomStrategy = Environment.getBestAvailableImplementation(RandomStrategy.class);

		randomStrategy.setSeed(1);
		BigInt random1 = BigIntFactory.random(20);
		randomStrategy.setSeed(1);
		BigInt random2 = BigIntFactory.random(20);

		assertThat(random1).isEqualTo(random2);
	}

	@Test
	public void testRandomRange1Bit() {
		checkRandomRange(1, 10);
	}

	@Test
	public void testRandomRange2Bit() {
		checkRandomRange(2, 30);
	}

	@Test
	public void testRandomRange6Bit() {
		checkRandomRange(5, 100);
	}

	private void checkRandomRange(int numBits, int repeat) {
		// Use seed for tests to prevent theoretical intermittent test failures.
		Environment.getBestAvailableImplementation(RandomStrategy.class).setSeed(0);

		BigInt lowestActual = BigIntFactory.random(numBits);
		BigInt highestActual = lowestActual;
		for (int i = 0; i < repeat; i++) {
			BigInt actual = BigIntFactory.random(numBits);
			if (actual.isLessThan(lowestActual)) {
				lowestActual = actual;
			}
			if (actual.isGreaterThan(highestActual)) {
				highestActual = actual;
			}
		}

		// TODO! modify this once bit shift ops have been implemented
		long expectedHighestLong = (1L << numBits) - 1;
		BigInt expectedHighest = from(expectedHighestLong);

		assertThat(lowestActual).isEqualTo(from(0));
		assertThat(highestActual).isEqualTo(expectedHighest);
	}
}
