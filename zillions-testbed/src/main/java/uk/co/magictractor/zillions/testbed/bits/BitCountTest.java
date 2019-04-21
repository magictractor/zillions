package uk.co.magictractor.zillions.testbed.bits;

import static uk.co.magictractor.zillions.core.environment.Environment.getBestAvailableImplementation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.api.bits.BigIntBitCount;

public class BitCountTest /*extends OpTestNoParam*/ {

//	public BitCountTest() {
//		super(BigInt::bitCount);
//	}

	@Test
	public void testZero() {
		check(0, 0);
	}

	@Test
	public void testMinimumPositive() {
		check(1, 1);
	}

	@Test
	public void testMinusOne() {
		check(-1, 0);
	}
	
	@Test
	public void testMinimumNegative() {
		check(-2, 1);
	}

	@Test
	public void testPositive() {
		check(0x99, 4);
	}

	@Test
	public void testNegative() {
		check(-0x99, 3);
	}
	
	private void check(long value, int expected) {
		check(BigIntFactory.from(value), expected);
	}

	private void check(BigInt value, int expected) {
		int actual = getBestAvailableImplementation(BigIntBitCount.class).bitCount(value);
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
