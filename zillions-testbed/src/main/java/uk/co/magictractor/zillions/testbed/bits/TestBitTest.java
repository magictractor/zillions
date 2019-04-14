package uk.co.magictractor.zillions.testbed.bits;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.testbed.OpTestSingleParam;

public class TestBitTest extends OpTestSingleParam<Integer, Boolean> {

	public TestBitTest() {
		super(Integer.class, Boolean.class, BigInt::testBit);
	}

	@Test
	public void testPositiveNumber() {
		check(9, 3, true);
	}

	@Test
	public void testPositiveNumberOffBit() {
		check(9, 2, false);
	}

	// -9 in binary is 111...1110111
	
	@Test
	public void testNegativeNumberOnBit() {
		check(-9, 2, true);
	}

	@Test
	public void testNegativeNumberOffBit() {
		check(-9, 3, false);
	}
	
	@Test
	public void testInvalidBitNum() {
		Assertions.assertThatThrownBy(() -> BigIntFactory.from(0).clearBit(-1))
				.isExactlyInstanceOf(ArithmeticException.class).hasMessage("Negative bit address");
	}

}
