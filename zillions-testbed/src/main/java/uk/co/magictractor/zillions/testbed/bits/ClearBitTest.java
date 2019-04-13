package uk.co.magictractor.zillions.testbed.bits;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.testbed.OpTestIntParam;

public class ClearBitTest extends OpTestIntParam {

	public ClearBitTest() {
		super(BigInt::clearBit);
	}

	@Test
	public void testPositiveNumberChangedBit() {
		check(9, 0, 8);
	}

	@Test
	public void testPositiveNumberUnchangedBit() {
		check(9, 1, 9);
	}

	// -9 in binary is 111...1110111

	@Test
	public void testNegativeNumberChangedBit() {
		check(-9, 0, -10);
	}

	@Test
	public void testNegativeNumberUnchangedBit() {
		check(-9, 3, -9);
	}
	
	@Test
	public void testInvalidBitNum() {
		Assertions.assertThatThrownBy(() -> BigIntFactory.from(0).clearBit(-1))
				.isExactlyInstanceOf(ArithmeticException.class).hasMessage("Negative bit address");
	}
}
