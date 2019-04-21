package uk.co.magictractor.zillions.testbed;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;

public abstract class OpTestSingleParam<PARAM, RESULT> extends OpTest<RESULT> {

	private final Class<PARAM> _paramClass;

	private final BiFunction<BigInt, PARAM, RESULT> _op;

	protected OpTestSingleParam(Class<PARAM> paramClass, Class<RESULT> resultClass,
			BiFunction<BigInt, PARAM, RESULT> op) {
		super(resultClass);
		_paramClass = paramClass;
		_op = op;
	}

	protected void check(long x, long y, long expected) {
		check(BigIntFactory.from(x), from(_paramClass, y), resultFrom(expected));
	}

	protected void check(String x, String y, String expected) {
		check(BigIntFactory.from(x), from(_paramClass, y), resultFrom(expected));
	}

	protected void check(long x, long y, boolean expected) {
		check(BigIntFactory.from(x), from(_paramClass, y), resultFrom(expected));
	}

	protected void check(BigInt x, PARAM y, RESULT expected) {
		RESULT actual = _op.apply(x, y);
		assertThat(actual).isEqualTo(expected);
		if (actual instanceof BigInt) {
			// BigInt ops which return a BigInt should always return itself.
			assertThat(actual).isSameAs(x);
		}
	}
}
