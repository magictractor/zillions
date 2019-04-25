package uk.co.magictractor.zillions.testbed.bits.shift;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.testbed.OpTestIntParam;

public class ShiftLeftTest extends OpTestIntParam {

    public ShiftLeftTest() {
        super(BigInt::shiftLeft);
    }

    @Test
    public void testPositiveNumberPositiveShift() {
        check(13, 2, 52);
    }

    @Test
    public void testNegativeNumberPositiveShift() {
        check(-13, 2, -52);
    }

    @Test
    public void testPositiveNumberNegativeShift() {
        check(13, -2, 3);
    }

    @Test
    public void testNegativeNumberNegativeShift() {
        check(-13, -2, -4);
    }
}
