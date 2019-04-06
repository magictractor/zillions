package uk.co.magictractor.zillions.testbed;

import static uk.co.magictractor.zillions.core.BigIntFactory.from;

import java.util.function.Consumer;

import org.assertj.core.api.Assertions;

import uk.co.magictractor.zillions.core.BigInt;

public class OpTestNoParam {

	private final Consumer<BigInt> _op;

	protected OpTestNoParam(Consumer<BigInt> op) {
		_op = op;
	}

	protected void check(long x, long expected) {
		check(from(x), from(expected));
	}

	protected void check(String x, String expected) {
		check(from(x), from(expected));
	}

	protected void check(BigInt x, BigInt expected) {
		_op.accept(x);
		Assertions.assertThat(x).isEqualTo(expected);
	}
}
