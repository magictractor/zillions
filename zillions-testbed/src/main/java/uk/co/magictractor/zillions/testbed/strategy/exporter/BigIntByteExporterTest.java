/**
 * Copyright 2015-2019 Ken Dobson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.magictractor.zillions.testbed.strategy.exporter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.jura.suite.WithinSuite;
import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.exporter.BigIntByteExporter;
import uk.co.magictractor.zillions.core.bits.BitUtils;
import uk.co.magictractor.zillions.environment.BigIntFactory;
import uk.co.magictractor.zillions.testbed.bigint.AbstractStrategyTest;

@WithinSuite
public class BigIntByteExporterTest extends AbstractStrategyTest<BigIntByteExporter> {

    protected BigIntByteExporterTest() {
        super(BigIntByteExporter.class);
    }

    @Test
    public void test0() {
        check(0, 0);
    }

    @Test
    public void test1() {
        check(1, 1);
    }

    @Test
    public void testMinus1() {
        check(-1, 255);
    }

    @Test
    public void testMinus2() {
        check(-2, 254);
    }

    @Test
    public void test127() {
        check(127, 127);
    }

    @Test
    public void test128() {
        check(128, 0, 128);
    }

    @Test
    public void test255() {
        check(255, 0, 255);
    }

    @Test
    public void test256() {
        check(256, 1, 0);
    }

    @Test
    public void testMinus127() {
        check(-127, 129);
    }

    @Test
    public void testMinus128() {
        check(-128, 128);
    }

    @Test
    public void testMinus255() {
        check(-255, 255, 1);
    }

    @Test
    public void testMinus256() {
        check(-256, 255, 0);
    }

    @Test
    public void testThreeBytePositiveWithTopBitSetInMostSignificantByte() {
        BigInt value = BigIntFactory.from(1).shiftLeft(23);
        check(value, 0, 128, 0, 0);
    }

    @Test
    public void testThreeByteNegative() {
        BigInt value = BigIntFactory.from(-1).shiftLeft(23);
        check(value, 128, 0, 0);
    }

    @Test
    public void testVeryLargePositive() {
        check("86739834739293766432993866432003874372993", 0, 254, 231, 204, 130, 114, 144, 95, 74, 89, 172, 56, 172,
            205, 35, 218, 61, 129);
    }

    @Test
    public void testVeryLargeNegative() {
        check("-8886449923764639200387382769755661028746268", 153, 253, 18, 31, 64, 157, 93, 175, 16, 72, 167, 219, 5,
            56, 201, 113, 27, 228);
    }

    private void check(int value, int... expectedBytesAsInts) {
        check(BigIntFactory.from(value), expectedBytesAsInts);
    }

    private void check(String value, int... expectedBytesAsInts) {
        check(BigIntFactory.from(value), expectedBytesAsInts);
    }

    private void check(BigInt value, int... expectedBytesAsInts) {
        checkAsBytes(value, expectedBytesAsInts);
        checkPopulateBytes(value, 1, leastSignificantBytes(1, expectedBytesAsInts));
        checkPopulateBytes(value, 6, leastSignificantBytes(6, expectedBytesAsInts));
        checkPopulateBytes(value, 16, leastSignificantBytes(16, expectedBytesAsInts));
        checkPopulateBytes(value, 128, leastSignificantBytes(128, expectedBytesAsInts));
    }

    private int[] leastSignificantBytes(int byteCount, int... expectedBytesAsInts) {
        int len = expectedBytesAsInts.length;
        //return Arrays.copyOfRange(expectedBytesAsInts, len - byteCount, len);
        int[] copy = new int[byteCount];
        if (byteCount > expectedBytesAsInts.length) {
            // Copy whole source. High bytes in destination are left as padding.
            if (BitUtils.isNegative(expectedBytesAsInts)) {
                // Negative numbers are padded with 0xff rather than zero.
                Arrays.fill(copy, 0xff);
            }
            System.arraycopy(expectedBytesAsInts, 0, copy, byteCount - len, len);
        }
        else {
            // High bytes in source are truncated (unless arrays have the same size).
            System.arraycopy(expectedBytesAsInts, len - byteCount, copy, 0, byteCount);
        }

        return copy;
    }

    private void checkAsBytes(BigInt value, int... expectedBytesAsInts) {
        byte[] actual = getImpl().asBytes(value);
        // Convert to ints to values in errors are in range 0-255 rather than -128-127
        int[] actualAsInts = BitUtils.bytesToInts(actual);
        Assertions.assertThat(actualAsInts).isEqualTo(expectedBytesAsInts);
    }

    private void checkPopulateBytes(BigInt value, int arrayLength, int... expectedBytesAsInts) {
        byte[] actual = new byte[arrayLength];
        // Populate with non-zero (and not 0xff) value for cases where array is larger than number length.
        Arrays.fill(actual, (byte) 0x55);

        // TODO! tests could wrap all impls checking that BigInts are not modified (unless also returned)
        int preHashCode = value.hashCode();
        getImpl().populateBytes(value, actual);
        assertThat(value.hashCode()).withFailMessage("value was modified").isEqualTo(preHashCode);

        // Convert to ints to values in errors are in range 0-255 rather than -128-127
        int[] actualAsInts = BitUtils.bytesToInts(actual);
        Assertions.assertThat(actualAsInts).isEqualTo(expectedBytesAsInts);
    }

}
