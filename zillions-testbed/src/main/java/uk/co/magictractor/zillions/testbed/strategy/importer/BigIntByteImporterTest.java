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
package uk.co.magictractor.zillions.testbed.strategy.importer;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.magictractor.zillions.environment.BigIntFactory.from;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.jura.suite.WithinSuite;
import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.importer.BigIntByteImporter;
import uk.co.magictractor.zillions.core.bits.BitUtils;
import uk.co.magictractor.zillions.environment.BigIntFactory;
import uk.co.magictractor.zillions.testbed.bigint.AbstractStrategyTest;

@WithinSuite
public class BigIntByteImporterTest extends AbstractStrategyTest<BigIntByteImporter> {

    protected BigIntByteImporterTest() {
        super(BigIntByteImporter.class);
    }

    @Test
    public void testZero() {
        check("0", 0x00);
    }

    @Test
    public void testSingleSetBit() {
        // 2^30
        check("1073741824", 0x40, 0x00, 0x00, 0x00);
    }

    @Test
    public void testOne() {
        check("1", 0x01);
    }

    @Test
    public void testMinusOneSingleByte() {
        check("-1", 0xff, 0xff, 0xff);
    }

    @Test
    public void testMinusOneMultipleBytes() {
        check("-1", 0xff, 0xff, 0xff);
    }

    @Test
    public void testMinusOneFourBytes() {
        check("-1", 0xff, 0xff, 0xff, 0xff);
    }

    @Test
    public void testNegativeByte() {
        // 136 unsigned
        check("-120", 0x88);
    }

    private void check(String expectedSignedString, int... bytesAsInts) {
        byte[] bytes = BitUtils.intsToBytes(bytesAsInts);

        BigInt expectedSigned = BigIntFactory.from(expectedSignedString);
        BigInt expectedUnsigned;
        if (expectedSigned.signum() >= 0) {
            expectedUnsigned = expectedSigned;
        }
        else {
            expectedUnsigned = from(1).shiftLeft(bytesAsInts.length * 8).add(expectedSigned);
        }

        Assertions.assertAll(() -> checkSigned(expectedSigned, bytes), () -> checkUnsigned(expectedUnsigned, bytes));
    }

    private void checkSigned(BigInt expectedSigned, byte[] bytes) {
        BigInt actualSigned = getImpl().signedFrom(from(0), bytes);
        assertThat(actualSigned).isEqualTo(expectedSigned);
    }

    private void checkUnsigned(BigInt expectedUnsigned, byte[] bytes) {
        BigInt actualUnsigned = getImpl().unsignedFrom(from(0), bytes);
        assertThat(actualUnsigned).isEqualTo(expectedUnsigned);
    }

}
