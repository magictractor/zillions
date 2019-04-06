package uk.co.magictractor.zillions.testbed;

import static uk.co.magictractor.zillions.core.BigIntFactory.from;

import java.util.function.BiConsumer;

import org.assertj.core.api.Assertions;

import uk.co.magictractor.zillions.core.BigInt;

public class OpTest {

	private final BiConsumer<BigInt, BigInt> _op;

	protected OpTest(BiConsumer<BigInt, BigInt> op) {
		_op = op;
	}

	protected void check(long x, long y, long expected) {
		check(from(x), from(y), from(expected));
	}

	protected void check(String x, String y, String expected) {
		check(from(x), from(y), from(expected));
	}

	protected void check(BigInt x, BigInt y, BigInt expected) {
		_op.accept(x, y);
		Assertions.assertThat(x).isEqualTo(expected);
	}
}
