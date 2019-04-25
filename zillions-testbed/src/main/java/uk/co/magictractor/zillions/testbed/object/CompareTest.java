package uk.co.magictractor.zillions.testbed.object;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.testbed.OpTestSingleParam;

public class CompareTest extends OpTestSingleParam<BigInt, Integer> {

    public CompareTest() {
        super(BigInt.class, Integer.class, BigInt::compareTo);
    }

    public void testEquals() {
        check(0, 0, 0);
        check(-10, -10, 0);
    }

    public void testLessThan() {
        check(0, 1, -1);
        check(-1, 0, -1);
    }

    public void testGreaterThan() {
        check(1, 0, 1);
        check(0, -1, -1);
    }

}
