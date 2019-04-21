package uk.co.magictractor.zillions.testbed;

import java.util.function.Function;

import org.assertj.core.api.Assertions;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;

public class OpTestNoParam<RESULT> extends OpTest<RESULT> {

	private final Function<BigInt, RESULT> _op;

	protected OpTestNoParam(Class<RESULT> resultClass, Function<BigInt, RESULT> op) {
		super(resultClass);
		_op = op;
	}

	protected void check(long x, long expected) {
		check(BigIntFactory.from(x), resultFrom(expected));
	}

	protected void check(BigInt x, long expected) {
		check(x, resultFrom(expected));
	}

	protected void check(String x, String expected) {
		check(BigIntFactory.from(x), resultFrom(expected));
	}

	protected void check(BigInt x, RESULT expected) {
		RESULT actual = _op.apply(x);
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
