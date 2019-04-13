package uk.co.magictractor.zillions.testbed.bits;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.testbed.OpTestIntParam;

public class SetBitTest extends OpTestIntParam {

	public SetBitTest() {
		super(BigInt::setBit);
	}

	@Test
	public void testPositiveNumberChangedBit() {
		check(9, 2, 13);
	}

	@Test
	public void testPositiveNumberUnchangedBit() {
		check(9, 3, 9);
	}

	// -9 in binary is 111...1110111

	@Test
	public void testNegativeNumberChangedBit() {
		check(-9, 3, -1);
	}

	@Test
	public void testNegativeNumberUnchangedBit() {
		check(-9, 2, -9);
	}

	@Test
	public void testInvalidBitNum() {
		Assertions.assertThatThrownBy(() -> BigIntFactory.from(0).setBit(-1))
				.isExactlyInstanceOf(ArithmeticException.class).hasMessage("Negative bit address");
	}

}
