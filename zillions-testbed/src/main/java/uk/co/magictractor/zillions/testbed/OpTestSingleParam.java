package uk.co.magictractor.zillions.testbed;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;

public abstract class OpTestSingleParam<PARAM, RESULT> {

	private final Class<PARAM> _paramClass;
	private final Class<RESULT> _resultClass;
	private final BiFunction<BigInt, PARAM, RESULT> _op;

	protected OpTestSingleParam(Class<PARAM> paramClass, Class<RESULT> resultClass,
			BiFunction<BigInt, PARAM, RESULT> op) {
		_paramClass = paramClass;
		_resultClass = resultClass;
		_op = op;
	}

	protected void check(long x, long y, long expected) {
		check(BigIntFactory.from(x), from(_paramClass, y), from(_resultClass, expected));
	}

	protected void check(String x, String y, String expected) {
		check(BigIntFactory.from(x), from(_paramClass, y), from(_resultClass, expected));
	}

	protected <T> T from(Class<T> targetClass, String value) {
		Object result;
		if (BigInt.class.equals(targetClass)) {
			result = BigIntFactory.from(value);
		} else {
			throw new IllegalStateException("Code needs modified to convert String to " + targetClass.getSimpleName());
		}
		return (T) result;
	}

	protected <T> T from(Class<T> targetClass, long value) {
		Object result;
		if (BigInt.class.equals(targetClass)) {
			result = BigIntFactory.from(value);
		} else if (Integer.class.equals(targetClass)) {
			// TODO! range assertions
			result = Integer.valueOf((int) value);
		} else {
			throw new IllegalStateException("Code needs modified to convert long to " + targetClass.getSimpleName());
		}
		return (T) result;
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
