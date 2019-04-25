package uk.co.magictractor.zillions.testbed.bits;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.testbed.OpTestIntParam;

public class FlipBitTest extends OpTestIntParam {

    public FlipBitTest() {
        super(BigInt::flipBit);
    }

    @Test
    public void testPositiveNumberFlipOn() {
        check(9, 2, 13);
    }

    @Test
    public void testPositiveNumberFlipOff() {
        check(9, 3, 1);
    }

    // -9 in binary is 111...1110111

    @Test
    public void testNegativeNumberFlipOn() {
        check(-9, 3, -1);
    }

    @Test
    public void testNegativeNumberFlipOff() {
        check(-9, 4, -25);
    }

    @Test
    public void testInvalidBitNum() {
        Assertions.assertThatThrownBy(() -> BigIntFactory.from(0).flipBit(-1)).isExactlyInstanceOf(
            ArithmeticException.class).hasMessage("Negative bit address");
    }
}
