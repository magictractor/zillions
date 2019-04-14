package uk.co.magictractor.zillions.testbed.bits.shift;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.testbed.OpTestIntParam;

public class ShiftRightTest extends OpTestIntParam {

	public ShiftRightTest() {
		super(BigInt::shiftRight);
	}

	@Test
	public void testPositiveNumberPositiveShift() {
		check(13, 2, 3);
	}

	@Test
	public void testNegativeNumberPositiveShift() {
		check(-13, 2, -4);
	}

	@Test
	public void testPositiveNumberNegativeShift() {
		check(13, -2, 52);
	}

	@Test
	public void testNegativeNumberNegativeShift() {
		check(-13, -2, -52);
	}
}
