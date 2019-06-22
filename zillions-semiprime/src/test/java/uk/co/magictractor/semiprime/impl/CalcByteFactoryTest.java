package uk.co.magictractor.semiprime.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.environment.BigIntFactory;

public class CalcByteFactoryTest {

    @Test
    public void testlowBytes_1() {
        List<CalcByte> actual = CalcByteFactory.lowBytes(1);

        assertContains(actual, 1, 1, 0);
        assertContains(actual, 27, 19, 2);
        assertContains(actual, 237, 229, 212);
        assertContains(actual, 255, 255, 254);

        assertThat(actual).hasSize(66);
    }

    @Test
    public void testlowBytes_255() {
        List<CalcByte> actual = CalcByteFactory.lowBytes(255);
        actual.forEach(System.err::println);

        assertContains(actual, 17, 15, 0);
        assertContains(actual, 251, 205, 200);
        assertContains(actual, 255, 1, 0);

        assertThat(actual).hasSize(64);
    }

    private void assertContains(List<CalcByte> calcBytes, int left, int right, int carried) {
        CalcByte calcByte = find(calcBytes, left, right);
        assertThat(calcByte.carried()).isEqualTo(BigIntFactory.from(carried));
    }

    private CalcByte find(List<CalcByte> calcBytes, int left, int right) {
        return calcBytes.stream().filter(c -> c.left() == left && c.right() == right).findAny().orElseThrow(
            () -> new AssertionError("No CalcByte for left=" + left + ", right=" + right));
    }

}
